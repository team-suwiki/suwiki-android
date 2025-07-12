package com.chukchukhaksa.mobile.common.webview

import androidx.compose.runtime.*

class WebViewState(
  initialUrl: String,
) {
  var url by mutableStateOf(initialUrl)
    private set
  
  var isLoading by mutableStateOf(false)
    internal set
  
  var pageTitle by mutableStateOf<String?>(null)
    internal set

  fun loadUrl(newUrl: String) {
    url = newUrl
  }
}

@Composable
fun rememberWebViewState(initialUrl: String): WebViewState {
  return remember { WebViewState(initialUrl) }
}