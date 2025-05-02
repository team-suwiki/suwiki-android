package com.suwiki.presentation.openmajor

import com.suwiki.presentation.openmajor.model.OpenMajor
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class OpenMajorState(
  val filteredAllOpenMajorList: PersistentList<OpenMajor> = persistentListOf(),
  val filteredBookmarkedOpenMajorList: PersistentList<OpenMajor> = persistentListOf(),
  val showBottomShadow: Boolean = true,
  val currentPage: Int = 0,
  val searchValue: String = "",
  val isLoading: Boolean = false,
) {
  val showAllOpenMajorEmptySearchResultScreen: Boolean = searchValue.isNotEmpty() && filteredAllOpenMajorList.isEmpty()
}

sealed interface OpenMajorSideEffect {
  data class HandleException(val throwable: Throwable) : OpenMajorSideEffect
  data object PopBackStack : OpenMajorSideEffect
  data class PopBackStackWithArgument(val argument: String) : OpenMajorSideEffect

  data object ShowNeedLoginToast : OpenMajorSideEffect
}
