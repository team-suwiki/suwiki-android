package com.chukchukhaksa.mobile.presentation.webview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chukchukhaksa.mobile.common.designsystem.component.appbar.SuwikiAppBarWithTitle
import com.chukchukhaksa.mobile.common.ui.collectWithLifecycle
import com.chukchukhaksa.mobile.common.webview.WebView
import com.chukchukhaksa.mobile.common.webview.WebViewCookie
import com.chukchukhaksa.mobile.common.webview.WebViewManager
import com.chukchukhaksa.mobile.common.webview.WebViewState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WebViewRoute(
  padding: PaddingValues,
  sharedWebViewState: WebViewState,
  onShowToast: (String) -> Unit,
  handleException: (Throwable) -> Unit,
  viewModel: WebViewViewModel = koinViewModel(),
) {
  val uiState by viewModel.mviStore.uiState.collectAsState()

  viewModel.mviStore.sideEffects.collectWithLifecycle { sideEffect ->
    when (sideEffect) {
      is WebViewSideEffect.ShowToast -> onShowToast(sideEffect.message)
    }
  }

  WebViewScreen(
    padding = padding,
    sharedWebViewState = sharedWebViewState,
  )
}

@Composable
fun WebViewScreen(
  padding: PaddingValues,
  sharedWebViewState: WebViewState,
) {
  LaunchedEffect(sharedWebViewState.isLoading) {
    if (sharedWebViewState.isLoading) return@LaunchedEffect

    try {
      Napier.i("=== WebView 쿠키 테스트 시작 ===")
      Napier.i("sharedWebViewState - isLoading: ${sharedWebViewState.isLoading}, pageTitle: ${sharedWebViewState.pageTitle}")

      val webView = WebViewManager.getSingletonWebView()

      // JavaScript로 쿠키 확인
      val cookieCheckScript = """
            try {
              var currentUrl = window.location.href;
              var currentDomain = window.location.hostname;

              var cookieInfo = "현재 페이지: " + currentUrl + "\n";
              cookieInfo += "현재 도메인: " + currentDomain + "\n";

              // 현재 페이지가 유효한 도메인인지 확인
              if (currentDomain && currentDomain !== "" && currentDomain !== "null") {
                var allCookies = document.cookie;
                cookieInfo += "브라우저에서 확인된 쿠키: " + allCookies + "\n";

                if (allCookies && allCookies.length > 0) {
                  cookieInfo += "\n개별 쿠키 확인:\n";
                  var cookies = allCookies.split(';');
                  for (var i = 0; i < cookies.length; i++) {
                    var cookie = cookies[i].trim();
                    if (cookie) {
                      cookieInfo += "- " + cookie + "\n";
                    }
                  }
                } else {
                  cookieInfo += "현재 도메인에서 확인된 쿠키가 없습니다.\n";
                  cookieInfo += "주의: httpOnly 쿠키는 JavaScript에서 접근할 수 없습니다.\n";
                }

                // 테스트 쿠키 설정 시도
                cookieInfo += "\nJavaScript로 테스트 쿠키 설정 시도 완료";

              } else {
                cookieInfo += "유효하지 않은 도메인이거나 페이지가 로드되지 않았습니다.\n";
                cookieInfo += "about:blank 또는 file:// 등의 스키마에서는 쿠키 접근이 제한됩니다.";
              }

              cookieInfo;
            } catch (error) {
              "쿠키 접근 오류: " + error.message + " (현재 URL: " + window.location.href + ")";
            }
          """.trimIndent()

      val result = WebViewManager.evaluateJavaScript(webView!!, cookieCheckScript)
      Napier.i("=== JavaScript 쿠키 확인 결과 ===")
      Napier.i(result ?: "JavaScript 실행 결과 없음")
      Napier.i("=== WebView 쿠키 테스트 완료 ===")

    } catch (e: Exception) {
      Napier.e("쿠키 설정/확인 중 오류: ${e.message}", e)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(padding),
  ) {
    SuwikiAppBarWithTitle(
      title = sharedWebViewState.pageTitle?.takeIf { it.isNotBlank() } ?: "수원대학교",
    )

    Box(
      modifier = Modifier.fillMaxSize(),
    ) {
      WebView(
          state = sharedWebViewState,
          modifier = Modifier.fillMaxSize(),
          useSingleton = true,
          cookies = listOf(
              WebViewCookie(
                  name = "test_normal_cookie",
                  value = "normal_value_123",
                  domain = ".google.com",
                  path = "/",
                  isSecure = false,
                  isHttpOnly = false,
              ),
              WebViewCookie(
                  name = "test_httponly_cookie",
                  value = "httponly_value_456",
                  domain = ".google.com",
                  path = "/",
                  isSecure = false,
                  isHttpOnly = true,
              ),
              WebViewCookie(
                  name = "test_secure_cookie",
                  value = "secure_value_789",
                  domain = ".google.com",
                  path = "/",
                  isSecure = true,
                  isHttpOnly = false,
              ),
          ),
      )

      if (sharedWebViewState.isLoading) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center,
        ) {
          CircularProgressIndicator()
        }
      }
    }
  }
}
