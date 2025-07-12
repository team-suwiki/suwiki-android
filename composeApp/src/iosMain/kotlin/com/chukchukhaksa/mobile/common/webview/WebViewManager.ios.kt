package com.chukchukhaksa.mobile.common.webview

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

@OptIn(ExperimentalForeignApi::class)
actual object WebViewManager {
  private var singletonWebView: WKWebView? = null
  private var isInitialized = false
  private var hasLoadedInitialUrl = false

  actual fun getSingletonWebView(): Any? {
    return singletonWebView
  }

  fun initializeSingletonWebView() {
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

  // 동기화를 위한 헬퍼 함수
  private inline fun <T> synchronized(lock: Any, block: () -> T): T {
    return block()
  }
}