package com.suwiki.presentation.timetable.openlecture

import android.content.Context
import com.suwiki.common.model.timetable.Cell
import com.suwiki.common.model.timetable.OpenLecture
import com.suwiki.common.model.timetable.TimetableCellColor
import com.suwiki.common.model.timetable.TimetableDay
import com.suwiki.presentation.timetable.R
import com.suwiki.presentation.timetable.navigation.argument.CellEditorArgument
import com.suwiki.presentation.timetable.openlecture.model.SchoolLevel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class OpenLectureState(
  val searchValue: String = "",
  val openLectureList: PersistentList<OpenLecture> = persistentListOf(),
  val selectedOpenMajor: String = "전체",
  val showSchoolLevelBottomSheet: Boolean = false,
  val schoolLevel: SchoolLevel = SchoolLevel.ALL,
  val showSelectCellColorBottomSheet: Boolean = false,
  val selectedTimetableCellColor: TimetableCellColor = TimetableCellColor.BROWN,
  val isLoading: Boolean = false,
  val lastUpdatedDate: String? = null,
)

fun List<Cell>.toText(context: Context): String {
  return if (isEmpty()) {
    context.getString(R.string.word_none)
  } else {
    joinToString(", ") { cell ->
      val periods = (cell.startPeriod..cell.endPeriod).joinToString(",") { it.toString() }
      context.getString(R.string.all_timetable_cell_screen_cell_info, cell.day.toText(context), periods, cell.location)
    }
  }
}

fun TimetableDay.toText(context: Context) = when (this) {
  TimetableDay.MON -> context.getString(R.string.word_mon)
  TimetableDay.TUE -> context.getString(R.string.word_tue)
  TimetableDay.WED -> context.getString(R.string.word_wed)
  TimetableDay.THU -> context.getString(R.string.word_thu)
  TimetableDay.FRI -> context.getString(R.string.word_fri)
  TimetableDay.SAT -> context.getString(R.string.word_sat)
  TimetableDay.E_LEARNING -> context.getString(R.string.word_elearning)
}

sealed interface OpenLectureSideEffect {
  data object ScrollToTop : OpenLectureSideEffect
  data object PopBackStack : OpenLectureSideEffect
  data class NavigateOpenMajor(val selectedOpenMajor: String) : OpenLectureSideEffect
  data class NavigateCellEditor(val argument: CellEditorArgument) : OpenLectureSideEffect
  data object NavigateAddCustomTimetableCell : OpenLectureSideEffect
  data class HandleException(val throwable: Throwable) : OpenLectureSideEffect
  data class ShowOverlapCellToast(val msg: String) : OpenLectureSideEffect
  data object ShowSuccessAddCellToast : OpenLectureSideEffect
}
