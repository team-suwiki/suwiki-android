package com.chukchukhaksa.mobile.common.webview

expect object WebViewManager {
  fun getSingletonWebView(): Any?
  fun createNewWebView(): Any
  fun isSingletonInitialized(): Boolean
  fun initializeSingletonWebView()
  fun loadUrlIfNeeded(webView: Any, url: String)
  
  // 쿠키 관리 함수들
  suspend fun setCookie(cookie: WebViewCookie)
  suspend fun setCookies(cookies: List<WebViewCookie>)
  suspend fun getCookies(domain: String): List<WebViewCookie>
  suspend fun removeCookie(name: String, domain: String)
  suspend fun removeAllCookies()
  
  // JavaScript 실행 함수
  suspend fun evaluateJavaScript(webView: Any, script: String): String?
}