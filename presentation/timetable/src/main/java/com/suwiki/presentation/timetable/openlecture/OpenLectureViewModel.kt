package com.suwiki.presentation.timetable.openlecture

import androidx.lifecycle.ViewModel
import com.suwiki.common.model.exception.TimetableCellOverlapException
import com.suwiki.common.model.timetable.OpenLecture
import com.suwiki.common.model.timetable.OpenLectureData
import com.suwiki.common.model.timetable.TimetableCell
import com.suwiki.common.model.timetable.TimetableCellColor
import com.suwiki.common.model.timetable.TimetableDay
import com.suwiki.domain.timetable.repository.OpenLectureRepository
import com.suwiki.domain.timetable.usecase.GetOpenLectureListUseCase
import com.suwiki.domain.timetable.usecase.InsertTimetableCellUseCase
import com.suwiki.domain.timetable.usecase.UpdateOpenLectureIfNeedUseCase
import com.suwiki.presentation.timetable.navigation.argument.toCellEditorArgument
import com.suwiki.presentation.timetable.openlecture.model.SchoolLevel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class OpenLectureViewModel(
  private val updateOpenLectureIfNeedUseCase: UpdateOpenLectureIfNeedUseCase,
  private val getOpenLectureListUseCase: GetOpenLectureListUseCase,
  private val insertTimetableCellUseCase: InsertTimetableCellUseCase,
  private val openLectureRepository: OpenLectureRepository,
) : ViewModel(), ContainerHost<OpenLectureState, OpenLectureSideEffect> {

  private val mutex: Mutex = Mutex()

  override val container: Container<OpenLectureState, OpenLectureSideEffect> = container(
    OpenLectureState(),
  )

  private val currentState
    get() = container.stateFlow.value

  private var searchQuery: String = ""
  private var isFirstVisit: Boolean = true

  private var selectedOpenLecture: OpenLecture? = null

  fun initData() = intent {
    if (isFirstVisit) {
      reduce { state.copy(isLoading = true) }
      updateOpenLectureIfNeedUseCase()
      val lastUpdated = openLectureRepository.getLastUpdatedDate()
      reduce {
        state.copy(
          lastUpdatedDate = lastUpdated,
        )
      }
      getOpenLectureList()
      isFirstVisit = false
    }
  }

  fun navigateCellEditor(openLecture: OpenLecture = OpenLecture()) = intent {
    postSideEffect(
      OpenLectureSideEffect.NavigateCellEditor(openLecture.toCellEditorArgument()),
    )
  }

  fun navigateAddCustomCell() = intent { postSideEffect(OpenLectureSideEffect.NavigateAddCustomTimetableCell) }

  fun insertTimetable() = intent {
    if (selectedOpenLecture == null) return@intent

    val timetableCellList = if (selectedOpenLecture!!.originalCellList.isEmpty()) {
      listOf(
        TimetableCell(
          name = selectedOpenLecture!!.name,
          professor = selectedOpenLecture!!.professorName,
          location = "",
          day = TimetableDay.E_LEARNING,
          startPeriod = 0,
          endPeriod = 0,
          color = state.selectedTimetableCellColor,
        ),
      )
    } else {
      selectedOpenLecture!!.originalCellList.map { cell ->
        TimetableCell(
          name = selectedOpenLecture!!.name,
          professor = selectedOpenLecture!!.professorName,
          location = cell.location,
          day = cell.day,
          startPeriod = cell.startPeriod,
          endPeriod = cell.endPeriod,
          color = state.selectedTimetableCellColor,
        )
      }
    }

    insertTimetableCellUseCase(timetableCellList)
      .onSuccess {
        postSideEffect(OpenLectureSideEffect.ShowSuccessAddCellToast)
      }
      .onFailure {
        if (it is TimetableCellOverlapException) {
          postSideEffect(OpenLectureSideEffect.ShowOverlapCellToast(it.message))
        } else {
          postSideEffect(OpenLectureSideEffect.HandleException(it))
        }
      }
  }

  fun updateSelectedCellColor(color: TimetableCellColor) = intent { reduce { state.copy(selectedTimetableCellColor = color) } }

  fun showSelectColorBottomSheet(openLecture: OpenLecture) = intent {
    reduce {
      selectedOpenLecture = openLecture
      state.copy(
        selectedTimetableCellColor = TimetableCellColor.entries.shuffled().first(),
        showSelectCellColorBottomSheet = true,
      )
    }
  }

  fun hideSelectColorBottomSheet() = intent { reduce { state.copy(showSelectCellColorBottomSheet = false) } }

  fun searchOpenLecture(search: String) {
    searchQuery = search
    getOpenLectureList(search = search)
  }

  @OptIn(OrbitExperimental::class)
  fun updateSearchValue(searchValue: String) = blockingIntent {
    reduce { state.copy(searchValue = searchValue) }
  }

  fun updateSchoolLevelPosition(schoolLevel: SchoolLevel) = intent {
    reduce {
      state.copy(
        schoolLevel = schoolLevel,
      )
    }

    getOpenLectureList()
  }

  fun updateSelectedOpenMajor(openMajor: String) = intent {
    if (openMajor == state.selectedOpenMajor) return@intent

    reduce {
      state.copy(
        selectedOpenMajor = openMajor,
      )
    }
    getOpenLectureList()
  }

  private fun getOpenLectureList(
    search: String = searchQuery,
    ) = intent {
    mutex.withLock {
      val newData = getOpenLectureListUseCase(
        GetOpenLectureListUseCase.Param(
          lectureOrProfessorName = search,
          major = if (currentState.selectedOpenMajor == "전체") null else currentState.selectedOpenMajor,
          grade = currentState.schoolLevel.query,
        ),
      ).catch {
        postSideEffect(OpenLectureSideEffect.HandleException(it))
      }.firstOrNull() ?: return@withLock

      reduce {
        state.copy(
          isLoading = false,
          openLectureList = newData
            .distinctBy { it.id }
            .toPersistentList(),
        )
      }
      postSideEffect(OpenLectureSideEffect.ScrollToTop)
    }
  }

  fun showGradeBottomSheet() = intent { reduce { state.copy(showSchoolLevelBottomSheet = true) } }
  fun hideGradeBottomSheet() = intent { reduce { state.copy(showSchoolLevelBottomSheet = false) } }
  fun popBackStack() = intent { postSideEffect(OpenLectureSideEffect.PopBackStack) }
}
