package com.chukchukhaksa.mobile

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import com.chukchukhaksa.mobile.common.webview.rememberWebViewState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import chukchukhaksa.composeapp.generated.resources.Res
import chukchukhaksa.composeapp.generated.resources.dialog_network_body
import chukchukhaksa.composeapp.generated.resources.dialog_network_header
import chukchukhaksa.composeapp.generated.resources.dialog_update_mandatory_body
import chukchukhaksa.composeapp.generated.resources.dialog_update_mandatory_header
import chukchukhaksa.composeapp.generated.resources.word_confirm
import com.chukchukhaksa.mobile.common.designsystem.component.dialog.SuwikiDialog
import com.chukchukhaksa.mobile.common.designsystem.component.toast.SuwikiToast
import com.chukchukhaksa.mobile.common.designsystem.theme.SuwikiTheme
import com.chukchukhaksa.mobile.common.designsystem.theme.White
import com.chukchukhaksa.mobile.common.kmp.SetStatusBarColor
import com.chukchukhaksa.mobile.common.ui.collectWithLifecycle
import com.chukchukhaksa.mobile.presentation.openmajor.navigation.OpenMajorRoute
import com.chukchukhaksa.mobile.presentation.openmajor.navigation.openMajorNavGraph
import com.chukchukhaksa.mobile.presentation.timetable.navigation.timetableNavGraph
import com.chukchukhaksa.mobile.presentation.webview.navigation.webViewNavGraph
import com.chukchukhaksa.mobile.common.designsystem.component.bottomnav.MainBottomNavigation
import com.chukchukhaksa.mobile.common.webview.WebViewCookie
import com.chukchukhaksa.mobile.common.webview.WebViewManager
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel = koinViewModel(),
  navigator: MainNavigator = rememberMainNavigator(),
) {
  SetStatusBarColor()

  LaunchedEffect(Unit) {
    WebViewManager.initializeSingletonWebView()
  }

  SuwikiTheme {
    KoinContext {
      val uiState = viewModel.mviStore.uiState.collectAsState().value
      val uriHandler = LocalUriHandler.current

      // Create shared WebView state that persists across navigation
      val sharedWebViewState = rememberWebViewState("https://www.google.com/")

      viewModel.mviStore.sideEffects.collectWithLifecycle { sideEffect ->
        when (sideEffect) {
          is MainSideEffect.OpenUrl -> uriHandler.openUri(sideEffect.url)
        }
      }

      LaunchedEffect(key1 = Unit) {
        viewModel.checkNeedForceUpdate()
      }

      Scaffold(
        containerColor = White,
        contentWindowInsets = WindowInsets(0.dp),
        modifier = modifier,
        bottomBar = {
          MainBottomNavigation(
            navController = navigator.navController,
            onNavigateToTimetable = navigator::navigateToTimetable,
            onNavigateToWebView = navigator::navigateToWebViewTab,
          )
        },
        content = { innerPadding ->
          NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
          ) {
            openMajorNavGraph(
              popBackStack = navigator::popBackStackIfNotHome,
              popBackStackWithArgument = { openMajor ->
                navigator.navController.previousBackStackEntry?.savedStateHandle?.set(
                  OpenMajorRoute.ARGUMENT_NAME,
                  openMajor,
                )
                navigator.popBackStackIfNotHome()
              },
              handleException = viewModel::handleException,
              onShowToast = viewModel::onShowToast,
            )

            timetableNavGraph(
              padding = innerPadding,
              argumentName = OpenMajorRoute.ARGUMENT_NAME,
              popBackStack = navigator::popBackStackIfNotHome,
              navigateTimetableEditor = navigator::navigateTimetableEditor,
              navigateTimetableList = navigator::navigateTimetableList,
              navigateOpenLecture = navigator::navigateOpenLecture,
              handleException = viewModel::handleException,
              onShowToast = viewModel::onShowToast,
              navigateOpenMajor = navigator::navigateOpenMajor,
              navigateCellEditor = navigator::navigateCellEditor,
            )

            webViewNavGraph(
              padding = innerPadding,
              sharedWebViewState = sharedWebViewState,
              handleException = viewModel::handleException,
              onShowToast = viewModel::onShowToast,
            )
          }

          if (uiState.showNetworkErrorDialog) {
            SuwikiDialog(
              headerText = stringResource(Res.string.dialog_network_header),
              bodyText = stringResource(Res.string.dialog_network_body),
              confirmButtonText = stringResource(Res.string.word_confirm),
              onDismissRequest = viewModel::hideNetworkErrorDialog,
              onClickConfirm = viewModel::hideNetworkErrorDialog,
            )
          }

          if (uiState.showForceUpdateDialog) {
            SuwikiDialog(
              headerText = stringResource(Res.string.dialog_update_mandatory_header),
              bodyText = stringResource(Res.string.dialog_update_mandatory_body),
              confirmButtonText = stringResource(Res.string.word_confirm),
              onDismissRequest = {},
              onClickConfirm = viewModel::openAppStore,
            )
          }

          SuwikiToast(
            visible = uiState.toastVisible,
            message = uiState.toastMessage,
          )
        },
      )
    }
  }
}
