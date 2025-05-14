package com.suwiki.presentation.openmajor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suwiki.common.model.exception.AuthorizationException
import com.suwiki.domain.openmajor.usecase.GetOpenMajorListUseCase
import com.suwiki.domain.timetable.repository.OpenLectureRepository
import com.suwiki.presentation.openmajor.model.toBookmarkedOpenMajorList
import com.suwiki.presentation.openmajor.model.toOpenMajorList
import com.suwiki.presentation.openmajor.navigation.OpenMajorRoute
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.joinAll
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

class OpenMajorViewModel(
  private val getOpenMajorListUseCase: GetOpenMajorListUseCase,
  private val openLectureRepository: OpenLectureRepository,
  savedStateHandle: SavedStateHandle,
) : ContainerHost<OpenMajorState, OpenMajorSideEffect>, ViewModel() {
  override val container: Container<OpenMajorState, OpenMajorSideEffect> = container(OpenMajorState())

  private var selectedOpenMajor = savedStateHandle[OpenMajorRoute.ARGUMENT_NAME] ?: "전체"
  private val allOpenMajorList = mutableListOf<String>()

  @OptIn(OrbitExperimental::class)
  fun updateSearchValue(searchValue: String) = blockingIntent {
    reduce { state.copy(searchValue = searchValue) }
    reduceOpenMajorList(searchValue)
  }

  fun syncPagerState(currentPage: Int) = intent {
    reduce { state.copy(currentPage = currentPage) }
  }

  fun updateSelectedOpenMajor(openMajor: String) = intent {
    selectedOpenMajor = openMajor
    reduceOpenMajorList()
  }

  fun popBackStack() = intent { postSideEffect(OpenMajorSideEffect.PopBackStack) }

  fun popBackStackWithArgument() = intent { postSideEffect(OpenMajorSideEffect.PopBackStackWithArgument(selectedOpenMajor)) }

  fun changeBottomShadowVisible(show: Boolean) = intent {
    reduce { state.copy(showBottomShadow = show) }
  }

  fun initData() = intent {
    reduce { state.copy(isLoading = true) }
    joinAll(getOpenMajor())
    reduce { state.copy(isLoading = false) }
  }

  private fun getOpenMajor() = intent {
    getOpenMajorListUseCase().onEach {
      allOpenMajorList.clear()
      val firebaseOpenMajor = openLectureRepository.getOpenMajor()
      allOpenMajorList.addAll((it + firebaseOpenMajor).distinct())
      reduceOpenMajorList()
    }.catch {
    }.launchIn(viewModelScope)
  }

  private fun reduceOpenMajorList(searchValue: String = container.stateFlow.value.searchValue) = intent {
    reduce {
      state.copy(
        filteredAllOpenMajorList = allOpenMajorList.toOpenMajorList(
          searchValue = searchValue,
          selectedOpenMajor = selectedOpenMajor,
        ),
      )
    }
  }
}
