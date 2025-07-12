package com.chukchukhaksa.mobile.common.webview

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebSettings
import android.webkit.WebView as AndroidWebView
import android.webkit.WebViewClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("StaticFieldLeak")
actual object WebViewManager {
  @Volatile
  private var singletonWebView: AndroidWebView? = null
  private var isInitialized = false
  private var hasLoadedInitialUrl = false

  @Volatile
  private var applicationContext: Context? = null

  @SuppressLint("SetJavaScriptEnabled")
  actual fun getSingletonWebView(): Any? {
    return singletonWebView
  }

  fun setApplicationContext(context: Context) {
    if (applicationContext == null) {
      applicationContext = context.applicationContext
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  actual fun initializeSingletonWebView() {
    val context = applicationContext
      ?: throw IllegalStateException("Application context not set. Call setApplicationContext() first from Android Application class.")

    if (singletonWebView == null) {
      synchronized(this) {
        if (singletonWebView == null) {
          singletonWebView = AndroidWebView(context).apply {
            settings.apply {
              javaScriptEnabled = true
              domStorageEnabled = true
              databaseEnabled = true
              builtInZoomControls = true
              displayZoomControls = false
              loadWithOverviewMode = true
              useWideViewPort = true
              cacheMode = WebSettings.LOAD_DEFAULT
            }
            webViewClient = object : WebViewClient() {
              override fun onPageStarted(
                view: AndroidWebView?,
                url: String?,
                favicon: android.graphics.Bitmap?
              ) {
                super.onPageStarted(view, url, favicon)
                // 글로벌 로딩 상태 업데이트 로직 필요시 추가
              }

              override fun onPageFinished(view: AndroidWebView?, url: String?) {
                super.onPageFinished(view, url)
                // 글로벌 상태 업데이트 로직 필요시 추가
              }
            }
          }
          isInitialized = true
        }
      }
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  fun initializeSingletonWebView(context: Context) {
    if (singletonWebView == null) {
      synchronized(this) {
        if (singletonWebView == null) {
          singletonWebView = AndroidWebView(context).apply {
            settings.apply {
              javaScriptEnabled = true
              domStorageEnabled = true
              databaseEnabled = true
              setSupportZoom(true)
              builtInZoomControls = true
              displayZoomControls = false
              loadWithOverviewMode = true
              useWideViewPort = true
              cacheMode = WebSettings.LOAD_DEFAULT
            }
            webViewClient = object : WebViewClient() {
              override fun onPageStarted(
                view: AndroidWebView?,
                url: String?,
                favicon: android.graphics.Bitmap?
              ) {
                super.onPageStarted(view, url, favicon)
                // 글로벌 로딩 상태 업데이트 로직 필요시 추가
              }

              override fun onPageFinished(view: AndroidWebView?, url: String?) {
                super.onPageFinished(view, url)
                // 글로벌 상태 업데이트 로직 필요시 추가
              }
            }
          }
          isInitialized = true
        }
      }
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  actual fun createNewWebView(): Any {
    // Context는 Compose에서 받아오므로 여기서는 임시로 null 반환
    throw IllegalStateException("Use createNewWebView(context) instead")
  }

  @SuppressLint("SetJavaScriptEnabled")
  fun createNewWebView(context: Context): AndroidWebView {
    return AndroidWebView(context).apply {
      settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = true
        databaseEnabled = true
        setSupportZoom(true)
        builtInZoomControls = true
        displayZoomControls = false
        loadWithOverviewMode = true
        useWideViewPort = true
        cacheMode = WebSettings.LOAD_DEFAULT
      }
    }
  }

  actual fun isSingletonInitialized(): Boolean {
    return isInitialized && singletonWebView != null
  }

  actual fun loadUrlIfNeeded(webView: Any, url: String) {
    if (webView is AndroidWebView) {
      if (webView == singletonWebView) {
        // 싱글톤 웹뷰인 경우 한 번만 로드
        if (!hasLoadedInitialUrl) {
          println("WebViewManager: 싱글톤 WebView URL 로드 - $url")
          webView.loadUrl(url)
          hasLoadedInitialUrl = true
        } else {
          println("WebViewManager: 싱글톤 WebView 이미 로드됨 - 현재 URL: ${webView.url}")
        }
        return
      }
      // 새로운 웹뷰인 경우 항상 로드
      println("WebViewManager: 새 WebView URL 로드 - $url")
      webView.loadUrl(url)
    }
  }

  // 쿠키 관리 함수들
  actual suspend fun setCookie(cookie: WebViewCookie) {
    val cookieManager = CookieManager.getInstance()
    val cookieString = buildCookieString(cookie)
    val url = if (cookie.isSecure) "https://${cookie.domain}" else "http://${cookie.domain}"

    suspendCancellableCoroutine<Unit> { continuation ->
      cookieManager.setCookie(url, cookieString) {
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
    val cookieManager = CookieManager.getInstance()
    val cookiesString = cookieManager.getCookie(domain) ?: return emptyList()

    return parseCookiesString(cookiesString, domain)
  }

  actual suspend fun removeCookie(name: String, domain: String) {
    val cookieManager = CookieManager.getInstance()
    val expiredCookie = "$name=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/"
    val url = "https://$domain"

    suspendCancellableCoroutine<Unit> { continuation ->
      cookieManager.setCookie(url, expiredCookie) {
        continuation.resume(Unit)
      }
    }
  }

  actual suspend fun removeAllCookies() {
    val cookieManager = CookieManager.getInstance()

    suspendCancellableCoroutine<Unit> { continuation ->
      cookieManager.removeAllCookies {
        continuation.resume(Unit)
      }
    }
  }

  private fun buildCookieString(cookie: WebViewCookie): String {
    val sb = StringBuilder()
    sb.append("${cookie.name}=${cookie.value}")

    if (cookie.path != "/") {
      sb.append("; path=${cookie.path}")
    }

    if (cookie.isSecure) {
      sb.append("; secure")
    }

    if (cookie.isHttpOnly) {
      sb.append("; httponly")
    }

    cookie.maxAge?.let { maxAge ->
      if (maxAge > 0) {
        val expiryDate = System.currentTimeMillis() + (maxAge * 1000)
        sb.append("; expires=${java.util.Date(expiryDate)}")
      }
    }

    return sb.toString()
  }

  private fun parseCookiesString(cookiesString: String, domain: String): List<WebViewCookie> {
    val cookies = mutableListOf<WebViewCookie>()
    val cookiePairs = cookiesString.split(";")

    cookiePairs.forEach { pair ->
      val trimmedPair = pair.trim()
      val nameValue = trimmedPair.split("=", limit = 2)
      if (nameValue.size == 2) {
        cookies.add(
          WebViewCookie(
            name = nameValue[0].trim(),
            value = nameValue[1].trim(),
            domain = domain
          )
        )
      }
    }

    return cookies
  }

  // JavaScript 실행 함수
  actual suspend fun evaluateJavaScript(webView: Any, script: String): String? {
    if (webView !is AndroidWebView) return null

    return suspendCancellableCoroutine { continuation ->
      webView.evaluateJavascript(script) { result ->
        continuation.resume(result)
      }
    }
  }
}
