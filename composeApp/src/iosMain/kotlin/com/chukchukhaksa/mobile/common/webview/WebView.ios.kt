package com.chukchukhaksa.mobile.common.webview

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.WebKit.WKWebView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebView(
  state: WebViewState,
  modifier: Modifier,
  useSingleton: Boolean,
) {
  // 싱글톤 웹뷰 초기화
  LaunchedEffect(useSingleton) {
    if (useSingleton) {
      WebViewManager.initializeSingletonWebView()
    }
  }

  UIKitView(
    factory = {
      val webView = if (useSingleton) {
        (WebViewManager.getSingletonWebView() as? WKWebView) ?: (WebViewManager.createNewWebView() as WKWebView)
      } else {
        WebViewManager.createNewWebView() as WKWebView
      }

      // URL 로딩 (필요한 경우에만)
      WebViewManager.loadUrlIfNeeded(webView, state.url)

      webView
    },
    modifier = modifier,
    update = { view ->
      if (view.URL?.absoluteString != state.url) {
        WebViewManager.loadUrlIfNeeded(view, state.url)
      }
    }
  )
}