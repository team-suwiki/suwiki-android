package com.suwiki.presentation.navigator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.suwiki.presentation.openmajor.navigation.navigateOpenMajor
import com.suwiki.presentation.timetable.navigation.TimetableRoute
import com.suwiki.presentation.timetable.navigation.argument.CellEditorArgument
import com.suwiki.presentation.timetable.navigation.argument.TimetableEditorArgument
import com.suwiki.presentation.timetable.navigation.navigateCellEditor
import com.suwiki.presentation.timetable.navigation.navigateOpenLecture
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
