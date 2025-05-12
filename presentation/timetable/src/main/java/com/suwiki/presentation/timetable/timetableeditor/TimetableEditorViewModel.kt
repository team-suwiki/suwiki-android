package com.suwiki.presentation.timetable.timetableeditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.suwiki.domain.timetable.usecase.InsertTimetableUseCase
import com.suwiki.domain.timetable.usecase.UpdateTimetableUseCase
import com.suwiki.presentation.common.ui.extension.decodeFromUri
import com.suwiki.presentation.timetable.navigation.TimetableRoute
import com.suwiki.presentation.timetable.navigation.argument.TimetableEditorArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

class TimetableEditorViewModel(
  private val insertTimetableUseCase: InsertTimetableUseCase,
  private val updateTimetableUseCase: UpdateTimetableUseCase,
  savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<TimetableEditorState, TimetableEditorSideEffect> {
  private val argument = savedStateHandle.get<String>(TimetableRoute.TIMETABLE_EDITOR_ARGUMENT)!!
  private val timetableEditorArgument = Json.decodeFromUri<TimetableEditorArgument>(argument)
  private val isEditMode = timetableEditorArgument.isEditMode
  override val container: Container<TimetableEditorState, TimetableEditorSideEffect> = container(
    timetableEditorArgument.toState(),
  )

  fun showSemesterBottomSheet() = intent { reduce { state.copy(isSheetOpenSemester = true) } }
  fun hideSemesterBottomSheet() = intent { reduce { state.copy(isSheetOpenSemester = false) } }
  fun updateSemesterPosition(position: Int) = intent { reduce { state.copy(selectedSemesterPosition = position) } }

  @OptIn(OrbitExperimental::class)
  fun updateName(name: String) = blockingIntent { reduce { state.copy(name = name) } }

  fun upsertTimetable() = intent {
    if (state.semester == null) {
      postSideEffect(TimetableEditorSideEffect.NeedSelectSemesterToast)
      return@intent
    }

    val useCase = if (isEditMode) {
      updateTimetableUseCase(
        param = UpdateTimetableUseCase.Param(
          createTime = timetableEditorArgument.createTime,
          name = state.name,
          year = state.semester!!.year,
          semester = state.semester!!.semester,
        ),
      )
    } else {
      insertTimetableUseCase(
        param = InsertTimetableUseCase.Param(
          name = state.name,
          year = state.semester!!.year,
          semester = state.semester!!.semester,
        ),
      )
    }

    useCase
      .onSuccess {
        postSideEffect(TimetableEditorSideEffect.PopBackStack)
      }.onFailure {
        postSideEffect(TimetableEditorSideEffect.HandleException(it))
      }
  }

  fun popBackStack() = intent { postSideEffect(TimetableEditorSideEffect.PopBackStack) }
}
