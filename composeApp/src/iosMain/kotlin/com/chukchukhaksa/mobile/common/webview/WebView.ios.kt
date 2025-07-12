package com.chukchukhaksa.mobile.common.webview

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.WebKit.WKNavigationDelegateProtocol
import platform.WebKit.WKWebView
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebView(
  state: WebViewState,
  modifier: Modifier,
  useSingleton: Boolean,
  cookies: List<WebViewCookie>,
) {
  // 페이지 로드 후 쿠키 설정을 위한 지연 처리
  LaunchedEffect(cookies, state.isLoading) {
    if (cookies.isNotEmpty() && !state.isLoading) {
      // 페이지 로드 완료 후 약간의 지연을 두고 쿠키 설정
      kotlinx.coroutines.delay(500)
      WebViewManager.setCookies(cookies)
    }
  }

  UIKitView(
    factory = {
      val webView = if (useSingleton) {
        WebViewManager.initializeSingletonWebView()
        (WebViewManager.getSingletonWebView() as? WKWebView) ?: (WebViewManager.createNewWebView() as WKWebView)
      } else {
        WebViewManager.createNewWebView() as WKWebView
      }

      // WKNavigationDelegate 설정으로 상태 업데이트
      webView.navigationDelegate = object : NSObject(), WKNavigationDelegateProtocol {

        override fun webView(webView: WKWebView, didFinishNavigation: platform.WebKit.WKNavigation?) {
          state.isLoading = false
          state.pageTitle = webView.title
        }
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
