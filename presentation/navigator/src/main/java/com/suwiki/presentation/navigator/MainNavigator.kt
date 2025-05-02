package com.suwiki.presentation.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.suwiki.presentation.lectureevaluation.navigation.navigateExamEvaluationEditor
import com.suwiki.presentation.lectureevaluation.navigation.navigateLectureEvaluation
import com.suwiki.presentation.lectureevaluation.navigation.navigateLectureEvaluationDetail
import com.suwiki.presentation.lectureevaluation.navigation.navigateLectureEvaluationEditor
import com.suwiki.presentation.lectureevaluation.navigation.navigateMyEvaluation
import com.suwiki.presentation.login.navigation.navigateFindId
import com.suwiki.presentation.login.navigation.navigateFindPassword
import com.suwiki.presentation.login.navigation.navigateLogin
import com.suwiki.presentation.myinfo.navigation.navigateBanHistory
import com.suwiki.presentation.myinfo.navigation.navigateMyAccount
import com.suwiki.presentation.myinfo.navigation.navigateMyInfo
import com.suwiki.presentation.myinfo.navigation.navigateMyPoint
import com.suwiki.presentation.myinfo.navigation.navigateQuit
import com.suwiki.presentation.myinfo.navigation.navigateResetPassword
import com.suwiki.presentation.notice.navigation.navigateNotice
import com.suwiki.presentation.notice.navigation.navigateNoticeDetail
import com.suwiki.presentation.openmajor.navigation.navigateOpenMajor
import com.suwiki.presentation.signup.navigation.navigateSignup
import com.suwiki.presentation.signup.navigation.navigateSignupComplete
import com.suwiki.presentation.timetable.navigation.TimetableRoute
import com.suwiki.presentation.timetable.navigation.argument.CellEditorArgument
import com.suwiki.presentation.timetable.navigation.argument.TimetableEditorArgument
import com.suwiki.presentation.timetable.navigation.navigateCellEditor
import com.suwiki.presentation.timetable.navigation.navigateOpenLecture
import com.suwiki.presentation.timetable.navigation.navigateTimetable
import com.suwiki.presentation.timetable.navigation.navigateTimetableEditor
import com.suwiki.presentation.timetable.navigation.navigateTimetableList

internal class MainNavigator(
  val navController: NavHostController,
) {
  val startDestination = TimetableRoute.route

  fun navigateOpenMajor(selectedOpenMajor: String) {
    navController.navigateOpenMajor(selectedOpenMajor)
  }

  fun navigateCellEditor(argument: CellEditorArgument) {
    navController.navigateCellEditor(argument)
  }

  fun navigateTimetableEditor(argument: TimetableEditorArgument = TimetableEditorArgument()) {
    navController.navigateTimetableEditor(argument)
  }

  fun navigateTimetableList() {
    navController.navigateTimetableList()
  }

  fun navigateOpenLecture() {
    navController.navigateOpenLecture()
  }

  fun popBackStackIfNotHome() {
    if (!isSameCurrentDestination(TimetableRoute.route)) {
      navController.popBackStack()
    }
  }

  private fun isSameCurrentDestination(route: String) =
    navController.currentDestination?.route == route

}

@Composable
internal fun rememberMainNavigator(
  navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
  MainNavigator(navController)
}
