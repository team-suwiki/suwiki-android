package com.chukchukhaksa.mobile.common.webview

import android.annotation.SuppressLint
import android.webkit.WebView as AndroidWebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun WebView(
  state: WebViewState,
  modifier: Modifier,
  useSingleton: Boolean,
) {
  val context = LocalContext.current

  // 싱글톤 웹뷰 초기화
  LaunchedEffect(useSingleton) {
    if (useSingleton) {
      WebViewManager.initializeSingletonWebView(context)
    }
  }

  AndroidView(
    factory = { ctx ->
      val webView = if (useSingleton) {
        (WebViewManager.getSingletonWebView() as? AndroidWebView) ?: WebViewManager.createNewWebView(ctx)
      } else {
        WebViewManager.createNewWebView(ctx)
      }

      // WebViewClient 설정 (싱글톤인 경우에만 state 업데이트)
      if (!useSingleton || webView.webViewClient == null) {
        webView.webViewClient = object : WebViewClient() {
          override fun onPageStarted(
            view: AndroidWebView?,
            url: String?,
            favicon: android.graphics.Bitmap?
          ) {
            super.onPageStarted(view, url, favicon)
            state.isLoading = true
          }

          override fun onPageFinished(view: AndroidWebView?, url: String?) {
            super.onPageFinished(view, url)
            state.isLoading = false
            state.pageTitle = view?.title
          }
        }
      }

      // URL 로딩 (필요한 경우에만)
      WebViewManager.loadUrlIfNeeded(webView, state.url)

      webView
    },
    modifier = modifier,
    update = { view ->
      if (view.url != state.url) {
        WebViewManager.loadUrlIfNeeded(view, state.url)
      }
    }
  )
}