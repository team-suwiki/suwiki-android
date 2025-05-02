package com.suwiki.presentation.timetable.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.suwiki.presentation.common.ui.extension.encodeToUri
import com.suwiki.presentation.timetable.celleditor.CellEditorRoute
import com.suwiki.presentation.timetable.navigation.argument.CellEditorArgument
import com.suwiki.presentation.timetable.navigation.argument.TimetableEditorArgument
import com.suwiki.presentation.timetable.openlecture.OpenLectureRoute
import com.suwiki.presentation.timetable.timetable.TimetableRoute
import com.suwiki.presentation.timetable.timetableeditor.TimetableEditorRoute
import com.suwiki.presentation.timetable.timetablelist.TimetableListRoute
import kotlinx.serialization.json.Json

fun NavController.navigateTimetableEditor(argument: TimetableEditorArgument = TimetableEditorArgument()) {
  navigate(TimetableRoute.timetableEditorRoute(Json.encodeToUri(argument)))
}

fun NavController.navigateTimetableList() {
  navigate(TimetableRoute.timetableListRoute)
}

fun NavController.navigateOpenLecture() {
  navigate(TimetableRoute.openLectureRoute)
}

fun NavController.navigateCellEditor(argument: CellEditorArgument = CellEditorArgument()) {
  navigate(TimetableRoute.cellEditorRoute(Json.encodeToUri(argument)))
}

fun NavGraphBuilder.timetableNavGraph(
  padding: PaddingValues,
  argumentName: String,
  popBackStack: () -> Unit,
  navigateOpenMajor: (String) -> Unit,
  navigateTimetableEditor: (TimetableEditorArgument) -> Unit,
  navigateTimetableList: () -> Unit,
  navigateOpenLecture: () -> Unit,
  navigateCellEditor: (CellEditorArgument) -> Unit,
  handleException: (Throwable) -> Unit,
  onShowToast: (String) -> Unit,
) {
  composable(route = TimetableRoute.route) {
    TimetableRoute(
      padding = padding,
      navigateTimetableEditor = { navigateTimetableEditor(TimetableEditorArgument()) },
      navigateTimetableList = navigateTimetableList,
      handleException = handleException,
      onShowToast = onShowToast,
      navigateOpenLecture = navigateOpenLecture,
      navigateCellEditor = navigateCellEditor,
    )
  }

  composable(
    route = TimetableRoute.timetableEditorRoute(
      "{${TimetableRoute.TIMETABLE_EDITOR_ARGUMENT}}",
    ),
    arguments = listOf(
      navArgument(TimetableRoute.TIMETABLE_EDITOR_ARGUMENT) {
        type = NavType.StringType
        nullable = true
      },
    ),
  ) {
    TimetableEditorRoute(
      popBackStack = popBackStack,
      handleException = handleException,
      onShowToast = onShowToast,
    )
  }

  composable(route = TimetableRoute.openLectureRoute) { navBackStackEntry ->
    val selectedOpenMajor = navBackStackEntry.savedStateHandle.get<String>(argumentName) ?: "전체"

    OpenLectureRoute(
      selectedOpenMajor = selectedOpenMajor,
      popBackStack = popBackStack,
      handleException = handleException,
      onShowToast = onShowToast,
      navigateOpenMajor = navigateOpenMajor,
      navigateCellEditor = navigateCellEditor,
    )
  }

  composable(
    route = TimetableRoute.cellEditorRoute("{${TimetableRoute.CELL_EDITOR_ARGUMENT}}"),
    arguments = listOf(
      navArgument(TimetableRoute.CELL_EDITOR_ARGUMENT) {
        type = NavType.StringType
        nullable = true
      },
    ),
  ) {
    CellEditorRoute(
      popBackStack = popBackStack,
      handleException = handleException,
      onShowToast = onShowToast,
    )
  }

  composable(
    route = TimetableRoute.timetableListRoute,
  ) {
    TimetableListRoute(
      handleException = handleException,
      popBackStack = popBackStack,
      navigateTimetableEditor = navigateTimetableEditor,
    )
  }
}

object TimetableRoute {
  const val route = "timetable"
  const val openLectureRoute = "open-lecture"
  const val timetableListRoute = "$route/list"
  const val CELL_EDITOR_ARGUMENT = "cell-editor-argument"
  const val TIMETABLE_EDITOR_ARGUMENT = "timetable-editor-argument"

  fun timetableEditorRoute(timetableEditor: String) = "$route/editor/$timetableEditor"
  fun cellEditorRoute(cellEditor: String) = "cell-editor/$cellEditor"
}
