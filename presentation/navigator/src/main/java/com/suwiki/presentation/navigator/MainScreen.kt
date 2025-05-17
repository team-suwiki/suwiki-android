package com.suwiki.presentation.navigator

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import com.suwiki.presentation.common.designsystem.component.dialog.SuwikiDialog
import com.suwiki.presentation.common.ui.extension.versionCode
import com.suwiki.presentation.common.ui.util.PLAY_STORE_SITE
import com.suwiki.presentation.navigator.component.SuwikiToast
import com.suwiki.presentation.openmajor.navigation.OpenMajorRoute
import com.suwiki.presentation.openmajor.navigation.openMajorNavGraph
import com.suwiki.presentation.timetable.navigation.timetableNavGraph
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MainScreen(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel = koinViewModel(),
  navigator: MainNavigator = rememberMainNavigator(),
) {
  val uiState = viewModel.collectAsState().value
  val uriHandler = LocalUriHandler.current
  val context = LocalContext.current
  viewModel.collectSideEffect { sideEffect ->
    when (sideEffect) {
      MainSideEffect.OpenPlayStoreSite -> uriHandler.openUri(PLAY_STORE_SITE)
    }
  }

  LaunchedEffect(key1 = Unit) {
    viewModel.checkUpdateMandatory(context.versionCode)
  }

  Scaffold(
    modifier = modifier,
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
      }

      if (uiState.showNetworkErrorDialog) {
        SuwikiDialog(
          headerText = stringResource(R.string.dialog_network_header),
          bodyText = stringResource(R.string.dialog_network_body),
          confirmButtonText = stringResource(id = com.suwiki.presentation.common.ui.R.string.word_confirm),
          onDismissRequest = viewModel::hideNetworkErrorDialog,
          onClickConfirm = viewModel::hideNetworkErrorDialog,
        )
      }

      if (uiState.showUpdateMandatoryDialog) {
        SuwikiDialog(
          headerText = stringResource(R.string.dialog_update_mandatory_header),
          bodyText = stringResource(R.string.dialog_update_mandatory_body),
          confirmButtonText = stringResource(id = com.suwiki.presentation.common.ui.R.string.word_confirm),
          onDismissRequest = {},
          onClickConfirm = viewModel::openPlayStoreSite,
        )
      }

      SuwikiToast(
        visible = uiState.toastVisible,
        message = uiState.toastMessage,
      )
    },
  )
}
