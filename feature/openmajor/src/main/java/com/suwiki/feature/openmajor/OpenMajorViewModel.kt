package com.suwiki.feature.openmajor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suwiki.core.model.exception.AuthorizationException
import com.suwiki.domain.openmajor.repository.OpenMajorRepository
import com.suwiki.domain.openmajor.usecase.GetBookmarkedOpenMajorListUseCase
import com.suwiki.domain.openmajor.usecase.GetOpenMajorListUseCase
import com.suwiki.domain.openmajor.usecase.RegisterBookmarkUseCase
import com.suwiki.domain.openmajor.usecase.UnRegisterBookmarkUseCase
import com.suwiki.domain.timetable.repository.OpenLectureRepository
import com.suwiki.feature.openmajor.model.OpenMajorTap
import com.suwiki.feature.openmajor.model.toBookmarkedOpenMajorList
import com.suwiki.feature.openmajor.model.toOpenMajorList
import com.suwiki.feature.openmajor.navigation.OpenMajorRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
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
import javax.inject.Inject

@HiltViewModel
class OpenMajorViewModel @Inject constructor(
  private val getOpenMajorListUseCase: GetOpenMajorListUseCase,
  private val getBookmarkedOpenMajorListUseCase: GetBookmarkedOpenMajorListUseCase,
  private val registerBookmarkUseCase: RegisterBookmarkUseCase,
  private val unRegisterBookmarkUseCase: UnRegisterBookmarkUseCase,
  private val openLectureRepository: OpenLectureRepository,
  savedStateHandle: SavedStateHandle,
) : ContainerHost<OpenMajorState, OpenMajorSideEffect>, ViewModel() {
  override val container: Container<OpenMajorState, OpenMajorSideEffect> = container(OpenMajorState())

  private var isLoggedIn: Boolean = true
  private var selectedOpenMajor = savedStateHandle[OpenMajorRoute.ARGUMENT_NAME] ?: "전체"
  private val allOpenMajorList = mutableListOf<String>()
  private val bookmarkedOpenMajorList = mutableListOf<String>()

  @OptIn(OrbitExperimental::class)
  fun updateSearchValue(searchValue: String) = blockingIntent {
    reduce { state.copy(searchValue = searchValue) }
    reduceOpenMajorList(searchValue)
    reduceBookmarkedOpenMajorList(searchValue)
  }

  fun toggleBookmark(openMajor: String) = intent {
    if (isLoggedIn.not()) {
      postSideEffect(OpenMajorSideEffect.ShowNeedLoginToast)
      return@intent
    }

    if (openMajor in bookmarkedOpenMajorList) {
      unRegisterBookmark(openMajor)
    } else {
      registerBookmark(openMajor)
    }
  }

  private fun registerBookmark(openMajor: String) = intent {
    registerBookmarkUseCase(openMajor)
      .onSuccess {
        bookmarkedOpenMajorList.add(openMajor)
        reduceBookmarkedOpenMajorList()
        reduceOpenMajorList()
      }
      .onFailure {
        postSideEffect(OpenMajorSideEffect.HandleException(it))
      }
  }

  private fun unRegisterBookmark(openMajor: String) = intent {
    unRegisterBookmarkUseCase(openMajor)
      .onSuccess {
        bookmarkedOpenMajorList.remove(openMajor)
        reduceBookmarkedOpenMajorList()
        reduceOpenMajorList()
      }
      .onFailure {
        postSideEffect(OpenMajorSideEffect.HandleException(it))
      }
  }

  fun syncPagerState(currentPage: Int) = intent {
    reduce { state.copy(currentPage = currentPage) }
  }

  fun updateSelectedOpenMajor(openMajor: String) = intent {
    selectedOpenMajor = openMajor
    reduceOpenMajorList()
    reduceBookmarkedOpenMajorList()
  }

  fun popBackStack() = intent { postSideEffect(OpenMajorSideEffect.PopBackStack) }

  fun popBackStackWithArgument() = intent { postSideEffect(OpenMajorSideEffect.PopBackStackWithArgument(selectedOpenMajor)) }

  fun changeBottomShadowVisible(show: Boolean) = intent {
    reduce { state.copy(showBottomShadow = show) }
  }

  fun initData() = intent {
    reduce { state.copy(isLoading = true) }
    joinAll(getOpenMajor(), getBookmarkedOpenMajor())
    reduce { state.copy(isLoading = false) }
  }

  private fun getOpenMajor() = intent {
    getOpenMajorListUseCase().onEach {
      allOpenMajorList.clear()
      val firebaseOpenMajor = openLectureRepository.getOpenMajor()
      allOpenMajorList.addAll((it + firebaseOpenMajor).distinct())
      reduceOpenMajorList()
    }.catch {
//      postSideEffect(OpenMajorSideEffect.HandleException(it))
    }.launchIn(viewModelScope)
  }

  private fun reduceOpenMajorList(searchValue: String = container.stateFlow.value.searchValue) = intent {
    reduce {
      state.copy(
        filteredAllOpenMajorList = allOpenMajorList.toOpenMajorList(
          searchValue = searchValue,
          bookmarkedOpenMajorList = bookmarkedOpenMajorList,
          selectedOpenMajor = selectedOpenMajor,
        ),
      )
    }
  }

  private fun getBookmarkedOpenMajor() = intent {
    getBookmarkedOpenMajorListUseCase()
      .onSuccess {
        bookmarkedOpenMajorList.addAll(it)
        reduceBookmarkedOpenMajorList()
        reduceOpenMajorList()
      }
      .onFailure {
        if (it is AuthorizationException) {
          isLoggedIn = false
        } else {
//          postSideEffect(OpenMajorSideEffect.HandleException(it))
        }
      }
  }

  private fun reduceBookmarkedOpenMajorList(searchValue: String = container.stateFlow.value.searchValue) = intent {
    reduce {
      state.copy(
        filteredBookmarkedOpenMajorList = bookmarkedOpenMajorList.toBookmarkedOpenMajorList(
          searchValue = searchValue,
          selectedOpenMajor = selectedOpenMajor,
        ),
      )
    }
  }
}
