package com.chukchukhaksa.mobile.presentation.webview.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chukchukhaksa.mobile.presentation.webview.WebViewRoute
import com.chukchukhaksa.mobile.common.webview.WebViewState

fun NavController.navigateWebView() {
  navigate(WebViewRoute.route)
}

fun NavGraphBuilder.webViewNavGraph(
  padding: PaddingValues,
  sharedWebViewState: WebViewState,
  handleException: (Throwable) -> Unit,
  onShowToast: (String) -> Unit,
) {
  composable(route = WebViewRoute.route) {
    WebViewRoute(
      padding = padding,
      sharedWebViewState = sharedWebViewState,
      handleException = handleException,
      onShowToast = onShowToast,
    )
  }
}

object WebViewRoute {
  const val route = "webview"
}