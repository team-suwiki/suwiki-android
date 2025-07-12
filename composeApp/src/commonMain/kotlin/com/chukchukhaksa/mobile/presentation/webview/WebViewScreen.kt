package com.chukchukhaksa.mobile.presentation.webview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chukchukhaksa.mobile.common.designsystem.component.appbar.SuwikiAppBarWithTitle
import com.chukchukhaksa.mobile.common.ui.collectWithLifecycle
import com.chukchukhaksa.mobile.common.webview.WebView
import com.chukchukhaksa.mobile.common.webview.WebViewState
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