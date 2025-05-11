package com.suwiki.presentation.timetable.celleditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.suwiki.common.model.exception.TimetableCellOverlapException
import com.suwiki.common.model.exception.TimetableCellPeriodInvalidException
import com.suwiki.common.model.timetable.TimetableCell
import com.suwiki.common.model.timetable.TimetableCellColor
import com.suwiki.common.model.timetable.TimetableDay
import com.suwiki.domain.timetable.usecase.InsertTimetableCellUseCase
import com.suwiki.domain.timetable.usecase.UpdateTimetableCellUseCase
import com.suwiki.presentation.common.ui.extension.decodeFromUri
import com.suwiki.presentation.timetable.navigation.TimetableRoute
import com.suwiki.presentation.timetable.navigation.argument.CellEditorArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

class CellEditorViewModel(
  private val insertTimetableCellUseCase: InsertTimetableCellUseCase,
  private val updateTimetableCellUseCase: UpdateTimetableCellUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<CellEditorState, CellEditorSideEffect> {
  private val argument = savedStateHandle.get<String>(TimetableRoute.CELL_EDITOR_ARGUMENT)!!
  private val cellEditorArgument = Json.decodeFromUri<CellEditorArgument>(argument)
  private val isEditMode = cellEditorArgument.isEditMode

  override val container: Container<CellEditorState, CellEditorSideEffect> = container(
    cellEditorArgument.toState(),
  )

  fun updateCellColor(color: TimetableCellColor) = intent {
    reduce { state.copy(selectedTimetableCellColor = color) }
  }

  fun addCell() = intent {
    reduce {
      state.copy(
        cellStateList = state.cellStateList.add(CellState()),
      )
    }
  }

  fun deleteCell(index: Int) = intent {
    reduce {
      state.copy(
        cellStateList = state.cellStateList.removeAt(index),
      )
    }
  }

  @OptIn(OrbitExperimental::class)
  fun updateCellLocation(index: Int, location: String) = blockingIntent {
    val toUpdateCell = state.cellStateList[index].copy(location = location)
    reduceCell(index, toUpdateCell)
  }

  @OptIn(OrbitExperimental::class)
  fun updateCellEndPeriod(index: Int, endPeriod: String) = blockingIntent {
    val toUpdateCell = state.cellStateList[index].copy(endPeriod = endPeriod.toPeriod())
    reduceCell(index, toUpdateCell)
  }

  @OptIn(OrbitExperimental::class)
  fun updateCellStartPeriod(index: Int, startPeriod: String) = blockingIntent {
    val toUpdateCell = state.cellStateList[index].copy(startPeriod = startPeriod.toPeriod())
    reduceCell(index, toUpdateCell)
  }

  private fun String.toPeriod(): String {
    return toIntOrNull()?.let {
      when {
        it < 1 -> "1"
        it > 15 -> "15"
        else -> this
      }
    } ?: ""
  }

  fun updateCellDay(index: Int, day: TimetableDay) = intent {
    val toUpdateCell = state.cellStateList[index].copy(day = day)
    reduceCell(index, toUpdateCell)
  }

  private suspend fun SimpleSyntax<CellEditorState, CellEditorSideEffect>.reduceCell(
    index: Int,
    toUpdateCell: CellState,
  ) {
    val cellList = state.cellStateList.removeAt(index)
    reduce {
      state.copy(
        cellStateList = cellList.add(index, toUpdateCell),
      )
    }
  }

  @OptIn(OrbitExperimental::class)
  fun updateLectureName(lectureName: String) = blockingIntent {
    reduce {
      state.copy(lectureName = lectureName)
    }
  }

  @OptIn(OrbitExperimental::class)
  fun updateProfessorName(professorName: String) = blockingIntent {
    reduce {
      state.copy(professorName = professorName)
    }
  }

  fun upsertTimetable() = intent {
    val sideEffect = when {
      state.lectureName.isEmpty() -> CellEditorSideEffect.ShowNeedLectureNameToast
      state.professorName.isEmpty() -> CellEditorSideEffect.ShowNeedProfessorNameToast
      state.cellStateList.any { it.location.isEmpty() } -> CellEditorSideEffect.ShowNeedLocationToast
      else -> null
    }

    sideEffect?.run {
      postSideEffect(this)
      return@intent
    }

    val timetableCellList = if (state.cellStateList.isEmpty()) {
      listOf(
        TimetableCell(
          name = state.lectureName,
          professor = state.professorName,
          location = "",
          day = TimetableDay.E_LEARNING,
          startPeriod = 0,
          endPeriod = 0,
          color = state.selectedTimetableCellColor,
        ),
      )
    } else {
      state.cellStateList.map { cell ->
        TimetableCell(
          name = state.lectureName,
          professor = state.professorName,
          location = cell.location,
          day = cell.day,
          startPeriod = cell.startPeriod.toIntOrNull() ?: 0,
          endPeriod = cell.endPeriod.toIntOrNull() ?: 0,
          color = state.selectedTimetableCellColor,
        )
      }
    }

    val useCase = if (isEditMode) {
      updateTimetableCellUseCase(
        UpdateTimetableCellUseCase.Param(
          cellEditorArgument.oldCellId,
          timetableCellList,
        ),
      )
    } else {
      insertTimetableCellUseCase(timetableCellList)
    }

    useCase
      .onSuccess {
        if (isEditMode) {
          postSideEffect(CellEditorSideEffect.ShowEditSuccessCellToast)
        } else {
          postSideEffect(CellEditorSideEffect.ShowAddSuccessCellToast)
        }

        popBackStack()
      }
      .onFailure { throwable ->
        when (throwable) {
          is TimetableCellOverlapException -> postSideEffect(CellEditorSideEffect.ShowToast(throwable.message))
          is TimetableCellPeriodInvalidException -> postSideEffect(CellEditorSideEffect.ShowToast(throwable.message))
          else -> postSideEffect(CellEditorSideEffect.HandleException(throwable))
        }
      }
  }

  fun popBackStack() = intent { postSideEffect(CellEditorSideEffect.PopBackStack) }
}
