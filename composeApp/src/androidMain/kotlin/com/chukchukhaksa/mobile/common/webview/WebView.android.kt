package com.chukchukhaksa.mobile.common.webview

import android.annotation.SuppressLint
import android.webkit.WebView as AndroidWebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun WebView(
  state: WebViewState,
  modifier: Modifier,
  useSingleton: Boolean,
  cookies: List<WebViewCookie>,
) {
  // 쿠키를 저장해둠 (페이지 로드 완료 후 설정)
  var pendingCookies by remember { mutableStateOf(cookies) }
  
  LaunchedEffect(cookies) {
    pendingCookies = cookies
  }

  AndroidView(
    factory = { ctx ->
      val webView = if (useSingleton) {
        (WebViewManager.getSingletonWebView() as? AndroidWebView) ?: WebViewManager.createNewWebView(ctx)
      } else {
        WebViewManager.createNewWebView(ctx)
      }

      webView.webViewClient = object : WebViewClient() {
        override fun onPageStarted(
          view: AndroidWebView?,
          url: String?,
          favicon: android.graphics.Bitmap?,
        ) {
          super.onPageStarted(view, url, favicon)
          state.isLoading = true
        }

        override fun onPageFinished(view: AndroidWebView?, url: String?) {
          super.onPageFinished(view, url)
          state.isLoading = false
          state.pageTitle = view?.title
          
          // 페이지 로드 완료 후 쿠키 설정
          if (pendingCookies.isNotEmpty()) {
            view?.post {
              CoroutineScope(Dispatchers.Main).launch {
                WebViewManager.setCookies(pendingCookies)
              }
            }
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
    },
  )
}
