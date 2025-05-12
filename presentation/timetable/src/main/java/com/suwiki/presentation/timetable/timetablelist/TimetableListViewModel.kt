package com.suwiki.presentation.timetable.timetablelist

import androidx.lifecycle.ViewModel
import com.suwiki.common.model.timetable.Timetable
import com.suwiki.domain.timetable.usecase.DeleteTimetableUseCase
import com.suwiki.domain.timetable.usecase.GetAllTimetableUseCase
import com.suwiki.domain.timetable.usecase.SetMainTimetableCreateTime
import com.suwiki.presentation.timetable.navigation.argument.toTimetableEditorArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.minus
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

class TimetableListViewModel(
  private val getAllTimetableUseCase: GetAllTimetableUseCase,
  private val deleteTimetableUseCase: DeleteTimetableUseCase,
  private val setMainTimetableCreateTime: SetMainTimetableCreateTime,
) : ViewModel(), ContainerHost<TimetableListState, TimetableListSideEffect> {
  override val container: Container<TimetableListState, TimetableListSideEffect> = container(TimetableListState())
  private var toDeleteTimetable: Timetable? = null

  fun initData() = intent {
    getAllTimetableUseCase()
      .onSuccess {
        reduce { state.copy(timetableList = it.toPersistentList()) }
      }
      .onFailure {
        postSideEffect(TimetableListSideEffect.HandleException(it))
      }
  }

  fun deleteTimetable() = intent {
    if (toDeleteTimetable == null) return@intent

    deleteTimetableUseCase(timetable = toDeleteTimetable!!)
      .onSuccess {
        reduce { state.copy(timetableList = state.timetableList.minus(toDeleteTimetable!!)) }
      }
      .onFailure {
        postSideEffect(TimetableListSideEffect.HandleException(it))
      }
    hideDeleteDialog()
  }

  fun popBackStack() = intent { postSideEffect(TimetableListSideEffect.PopBackStack) }
  fun navigateTimetableEditor(timetable: Timetable) = intent {
    postSideEffect(TimetableListSideEffect.NavigateTimetableEditor(timetable.toTimetableEditorArgument()))
  }

  fun showDeleteDialog(timetable: Timetable) = intent {
    toDeleteTimetable = timetable
    reduce { state.copy(showDeleteDialog = true) }
  }

  fun hideDeleteDialog() = intent {
    toDeleteTimetable = null
    reduce { state.copy(showDeleteDialog = false) }
  }

  fun setMainTimetable(createTime: Long) = intent {
    setMainTimetableCreateTime(createTime)
      .onSuccess { popBackStack() }
      .onFailure { postSideEffect(TimetableListSideEffect.HandleException(it)) }
  }
}
