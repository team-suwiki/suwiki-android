package com.chukchukhaksa.mobile.presentation.webview

data class WebViewState(
  val url: String = "https://www.suwon.ac.kr",
  val isLoading: Boolean = false,
  val title: String = "",
)

sealed interface WebViewSideEffect {
  data class ShowToast(val message: String) : WebViewSideEffect
}