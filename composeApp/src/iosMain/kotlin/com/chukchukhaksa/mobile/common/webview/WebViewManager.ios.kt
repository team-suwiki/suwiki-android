package com.chukchukhaksa.mobile.common.webview

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreGraphics.CGRectZero
import platform.Foundation.*
import platform.WebKit.WKHTTPCookieStore
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual object WebViewManager {
  private var singletonWebView: WKWebView? = null
  private var isInitialized = false
  private var hasLoadedInitialUrl = false

  actual fun getSingletonWebView(): Any? {
    return singletonWebView
  }

  actual fun initializeSingletonWebView() {
    if (singletonWebView == null) {
      synchronized(this) {
        if (singletonWebView == null) {
          val config = WKWebViewConfiguration()
          singletonWebView = WKWebView(frame = cValue { CGRectZero }, configuration = config)
          isInitialized = true
        }
      }
    }
  }

  actual fun createNewWebView(): Any {
    val config = WKWebViewConfiguration()
    return WKWebView(frame = cValue { CGRectZero }, configuration = config)
  }

  actual fun isSingletonInitialized(): Boolean {
    return isInitialized && singletonWebView != null
  }

  actual fun loadUrlIfNeeded(webView: Any, url: String) {
    if (webView is WKWebView) {
      if (webView == singletonWebView) {
        // 싱글톤 웹뷰인 경우
        if (!hasLoadedInitialUrl) {
          val nsUrl = NSURL.URLWithString(url)
          if (nsUrl != null) {
            val request = NSURLRequest.requestWithURL(nsUrl)
            webView.loadRequest(request)
            hasLoadedInitialUrl = true
          }
        }
        return
      }
      // 새로운 웹뷰인 경우 항상 로드
      val nsUrl = NSURL.URLWithString(url)
      if (nsUrl != null) {
        val request = NSURLRequest.requestWithURL(nsUrl)
        webView.loadRequest(request)
      }
    }
  }

  // 쿠키 관리 함수들
  actual suspend fun setCookie(cookie: WebViewCookie) {
    val cookieStore = getCookieStore()
    val nsHttpCookie = createNSHTTPCookie(cookie)

    suspendCancellableCoroutine<Unit> { continuation ->
      cookieStore.setCookie(nsHttpCookie) {
        continuation.resume(Unit)
      }
    }
  }

  actual suspend fun setCookies(cookies: List<WebViewCookie>) {
    cookies.forEach { cookie ->
      setCookie(cookie)
    }
  }

  actual suspend fun getCookies(domain: String): List<WebViewCookie> {
    val cookieStore = getCookieStore()

    return suspendCancellableCoroutine { continuation ->
      cookieStore.getAllCookies { nsHttpCookies ->
        val cookies = nsHttpCookies?.mapNotNull { cookie ->
          val nsHttpCookie = cookie as? NSHTTPCookie
          if (nsHttpCookie?.domain == domain || nsHttpCookie?.domain == ".$domain") {
            WebViewCookie(
              name = nsHttpCookie.name,
              value = nsHttpCookie.value,
              domain = nsHttpCookie.domain,
              path = nsHttpCookie.path,
              isSecure = nsHttpCookie.isSecure(),
              isHttpOnly = nsHttpCookie.isHTTPOnly()
            )
          } else null
        } ?: emptyList()

        continuation.resume(cookies)
      }
    }
  }

  actual suspend fun removeCookie(name: String, domain: String) {
    val cookieStore = getCookieStore()
    val cookies = getCookies(domain)

    cookies.firstOrNull { it.name == name }?.let { cookie ->
      val nsHttpCookie = createNSHTTPCookie(cookie)
      suspendCancellableCoroutine<Unit> { continuation ->
        cookieStore.deleteCookie(nsHttpCookie) {
          continuation.resume(Unit)
        }
      }
    }
  }

  actual suspend fun removeAllCookies() {
    val cookieStore = getCookieStore()

    suspendCancellableCoroutine<Unit> { continuation ->
      cookieStore.getAllCookies { cookies ->
        val cookiesToDelete = cookies ?: emptyList<Any>()
        var deletedCount = 0
        val totalCount = cookiesToDelete.size

        if (totalCount == 0) {
          continuation.resume(Unit)
          return@getAllCookies
        }

        cookiesToDelete.forEach { cookie ->
          val nsHttpCookie = cookie as NSHTTPCookie
          cookieStore.deleteCookie(nsHttpCookie) {
            deletedCount++
            if (deletedCount == totalCount) {
              continuation.resume(Unit)
            }
          }
        }
      }
    }
  }

  private fun getCookieStore(): WKHTTPCookieStore {
    val webView = singletonWebView ?: createNewWebView() as WKWebView
    return webView.configuration.websiteDataStore.httpCookieStore
  }

  private fun createNSHTTPCookie(cookie: WebViewCookie): NSHTTPCookie {
    val cookieProperties = mutableMapOf<Any?, Any?>()
    cookieProperties[NSHTTPCookieName] = cookie.name
    cookieProperties[NSHTTPCookieValue] = cookie.value
    cookieProperties[NSHTTPCookieDomain] = cookie.domain
    cookieProperties[NSHTTPCookiePath] = cookie.path

    if (cookie.isSecure) {
      cookieProperties[NSHTTPCookieSecure] = true
    }

    if (cookie.isHttpOnly) {
      // iOS에서 HttpOnly 속성 설정 - 여러 방법 시도
      cookieProperties["HttpOnly"] = true
      cookieProperties["HTTPOnly"] = true
      cookieProperties[NSString.stringWithString("HttpOnly")] = true
    }

    // maxAge는 iOS에서 생략 (세션 쿠키로 처리)

    return NSHTTPCookie.cookieWithProperties(cookieProperties as Map<Any?, *>)!!
  }

  // JavaScript 실행 함수
  actual suspend fun evaluateJavaScript(webView: Any, script: String): String? {
    if (webView !is WKWebView) return null

    return suspendCancellableCoroutine { continuation ->
      webView.evaluateJavaScript(script) { result, error ->
        if (error != null) {
          continuation.resume(null)
        } else {
          continuation.resume(result?.toString())
        }
      }
    }
  }

  // 동기화를 위한 헬퍼 함수
  private inline fun <T> synchronized(lock: Any, block: () -> T): T {
    return block()
  }
}
