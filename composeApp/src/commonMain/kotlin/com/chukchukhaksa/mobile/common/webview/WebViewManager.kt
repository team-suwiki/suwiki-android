package com.chukchukhaksa.mobile.common.webview

expect object WebViewManager {
  fun getSingletonWebView(): Any?
  fun createNewWebView(): Any
  fun isSingletonInitialized(): Boolean
  fun loadUrlIfNeeded(webView: Any, url: String)
}