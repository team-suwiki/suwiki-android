package com.suwiki.feature.navigator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.suwiki.core.designsystem.component.dialog.SuwikiDialog
import com.suwiki.core.designsystem.shadow.bottomNavigationShadow
import com.suwiki.core.designsystem.theme.Black
import com.suwiki.core.designsystem.theme.Blue100
import com.suwiki.core.designsystem.theme.Gray95
import com.suwiki.core.designsystem.theme.GrayDA
import com.suwiki.core.designsystem.theme.Primary
import com.suwiki.core.designsystem.theme.SuwikiTheme
import com.suwiki.core.designsystem.theme.White
import com.suwiki.core.ui.extension.suwikiClickable
import com.suwiki.core.ui.extension.versionCode
import com.suwiki.core.ui.util.ASK_SITE
import com.suwiki.core.ui.util.PLAY_STORE_SITE
import com.suwiki.core.ui.util.PRIVACY_POLICY_SITE
import com.suwiki.feature.lectureevaluation.editor.navigation.myEvaluationEditNavGraph
import com.suwiki.feature.lectureevaluation.my.navigation.myEvaluationNavGraph
import com.suwiki.feature.lectureevaluation.viewerreporter.navigation.lectureEvaluationNavGraph
import com.suwiki.feature.login.navigation.loginNavGraph
import com.suwiki.feature.myinfo.navigation.myInfoNavGraph
import com.suwiki.feature.navigator.component.SuwikiToast
import com.suwiki.feature.notice.navigation.noticeNavGraph
import com.suwiki.feature.openmajor.navigation.OpenMajorRoute
import com.suwiki.feature.openmajor.navigation.openMajorNavGraph
import com.suwiki.feature.signup.navigation.signupNavGraph
import com.suwiki.feature.timetable.navigation.timetableNavGraph
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MainScreen(
  modifier: Modifier = Modifier,
  viewModel: MainViewModel = hiltViewModel(),
  navigator: MainNavigator = rememberMainNavigator(),
) {
  val uiState = viewModel.collectAsState().value
  val uriHandler = LocalUriHandler.current
  val context = LocalContext.current
  val needShowEndPopup by viewModel.needShowSuwikiServiceEnd.collectAsStateWithLifecycle(false)
  var closeEndPopupOnce by remember { mutableStateOf(false) }

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
          confirmButtonText = stringResource(id = com.suwiki.core.ui.R.string.word_confirm),
          onDismissRequest = viewModel::hideNetworkErrorDialog,
          onClickConfirm = viewModel::hideNetworkErrorDialog,
        )
      }

      if (uiState.showUpdateMandatoryDialog) {
        SuwikiDialog(
          headerText = stringResource(R.string.dialog_update_mandatory_header),
          bodyText = stringResource(R.string.dialog_update_mandatory_body),
          confirmButtonText = stringResource(id = com.suwiki.core.ui.R.string.word_confirm),
          onDismissRequest = {},
          onClickConfirm = viewModel::openPlayStoreSite,
        )
      }

      if (needShowEndPopup && closeEndPopupOnce.not()) {
        Dialog(
          onDismissRequest = {},
          content = {
            Column(
              modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(White)
                .padding(top = 20.dp, bottom = 15.dp, start = 15.dp, end = 22.dp),
            ) {
              Text(
                text = "SUWIKI 서비스 종료 및 WISU 출시 안내",
                style = SuwikiTheme.typography.header5,
                color = Black,
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = """
                      SUWIKI는 많은 관심 속에 운영을 이어왔고,
                      이제 새롭게 WISU로 다시 시작하고자합니다!

                      기존 SUWIKI와는 전혀 다른 별도의 서비스예요.
                      (iOS 버전은 7월 출시 예정! 🚀)

                      예전 SUWIKI iOS가 불안정했던 거 아시죠?
                      이번엔 새로운 개발진과 함께 안정성에 진심입니다. 믿어주세요 🙌

                      기존 SUWIKI 관련 문의나 정책은 아래 링크 참고해주세요.

              """.trimIndent(),
                style = SuwikiTheme.typography.body5,
                color = Black,
              )

              Text(
                modifier = Modifier.suwikiClickable(
                  onClick = {
                    uriHandler.openUri(ASK_SITE)
                  },
                ),
                text = """
                      SUWIKI 문의하기
              """.trimIndent(),
                style = SuwikiTheme.typography.body5.copy(textDecoration = TextDecoration.Underline),
                color = Blue100,
              )

              Spacer(Modifier.height(8.dp))

              Text(
                modifier = Modifier.suwikiClickable(
                  onClick = {
                    uriHandler.openUri(PRIVACY_POLICY_SITE)
                  },
                ),
                text = """
                      SUWIKI 개인정보처리방침
              """.trimIndent(),
                style = SuwikiTheme.typography.body5.copy(textDecoration = TextDecoration.Underline),
                color = Blue100,
              )

              Text(
                text = """

                      잘 부탁드릴게요!
              """.trimIndent(),
                style = SuwikiTheme.typography.body5,
                color = Black,
              )
              Spacer(modifier = Modifier.height(37.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
              ) {
                Text(
                  modifier = Modifier.suwikiClickable(rippleEnabled = false, onClick = { closeEndPopupOnce = true }),
                  text = "닫기",
                  style = SuwikiTheme.typography.body4,
                  color = Gray95,
                )

                Spacer(modifier = Modifier.width(30.dp))

                Text(
                  modifier = Modifier.suwikiClickable(rippleEnabled = false, onClick = { viewModel.neverShow() }),
                  text = "다시보지 않기",
                  style = SuwikiTheme.typography.body4,
                  color = Primary,
                )
              }
            }
          },
        )
      }


      SuwikiToast(
        visible = uiState.toastVisible,
        message = uiState.toastMessage,
      )
    },
  )
}

@Composable
private fun MainBottomBar(
  visible: Boolean,
  tabs: ImmutableList<MainTab>,
  currentTab: MainTab?,
  onTabSelected: (MainTab) -> Unit,
) {
  AnimatedVisibility(
    visible = visible,
    enter = fadeIn() + slideIn { IntOffset(0, it.height) },
    exit = fadeOut() + slideOut { IntOffset(0, it.height) },
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .bottomNavigationShadow()
        .background(
          color = White,
          shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        ),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      tabs.forEach { tab ->
        MainBottomBarItem(
          tab = tab,
          selected = tab == currentTab,
          onClick = { onTabSelected(tab) },
        )
      }
    }
  }
}

@Composable
private fun RowScope.MainBottomBarItem(
  tab: MainTab,
  selected: Boolean,
  onClick: () -> Unit,
) {
  Box(
    modifier = Modifier
      .weight(1f)
      .fillMaxHeight()
      .suwikiClickable(
        rippleEnabled = false,
        onClick = onClick,
      ),
    contentAlignment = Alignment.Center,
  ) {
    Icon(
      painter = painterResource(tab.iconResId),
      contentDescription = tab.contentDescription,
      tint = if (selected) {
        Primary
      } else {
        GrayDA
      },
      modifier = Modifier.size(24.dp),
    )
  }
}
