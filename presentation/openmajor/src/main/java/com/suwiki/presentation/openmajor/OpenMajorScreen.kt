package com.suwiki.presentation.openmajor

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suwiki.presentation.common.designsystem.component.appbar.SuwikiAppBarWithTitle
import com.suwiki.presentation.common.designsystem.component.button.SuwikiContainedLargeButton
import com.suwiki.presentation.common.designsystem.component.loading.LoadingScreen
import com.suwiki.presentation.common.designsystem.component.searchbar.SuwikiSearchBar
import com.suwiki.presentation.common.designsystem.component.tabbar.SuwikiTabBar
import com.suwiki.presentation.common.designsystem.component.tabbar.TabTitle
import com.suwiki.presentation.common.designsystem.shadow.suwikiShadow
import com.suwiki.presentation.common.designsystem.theme.Gray95
import com.suwiki.presentation.common.designsystem.theme.SuwikiTheme
import com.suwiki.presentation.common.designsystem.theme.White
import com.suwiki.presentation.common.ui.extension.collectWithLifecycle
import com.suwiki.presentation.common.ui.extension.isScrolledToEnd
import com.suwiki.presentation.openmajor.component.OpenMajorContainer
import com.suwiki.presentation.openmajor.model.OpenMajor
import com.suwiki.presentation.openmajor.model.OpenMajorTap
import kotlinx.collections.immutable.PersistentList
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private val OPEN_MAJOR_PAGE_COUNT = OpenMajorTap.entries.size

@Composable
fun OpenMajorRoute(
  viewModel: OpenMajorViewModel = koinViewModel(),
  popBackStack: () -> Unit,
  popBackStackWithArgument: (String) -> Unit,
  handleException: (Throwable) -> Unit,
  onShowToast: (String) -> Unit,
) {
  val uiState = viewModel.collectAsState().value

  val context = LocalContext.current
  viewModel.collectSideEffect { sideEffect ->
    when (sideEffect) {
      is OpenMajorSideEffect.HandleException -> handleException(sideEffect.throwable)
      OpenMajorSideEffect.PopBackStack -> popBackStack()
      is OpenMajorSideEffect.PopBackStackWithArgument -> popBackStackWithArgument(sideEffect.argument)
      is OpenMajorSideEffect.ShowNeedLoginToast -> {
        onShowToast(context.getString(R.string.open_major_need_login_toast))
      }
    }
  }

  val pagerState = rememberPagerState(pageCount = { OPEN_MAJOR_PAGE_COUNT })

  LaunchedEffect(key1 = uiState.currentPage) {
    pagerState.animateScrollToPage(uiState.currentPage)
  }

  snapshotFlow { pagerState.currentPage }.collectWithLifecycle {
    viewModel.syncPagerState(it)
  }

  val allOpenMajorListState = rememberLazyListState()

  val onReachedBottomAllOpenMajor by remember {
    derivedStateOf {
      allOpenMajorListState.isScrolledToEnd()
    }
  }


  LaunchedEffect(onReachedBottomAllOpenMajor) {
    viewModel.changeBottomShadowVisible(!onReachedBottomAllOpenMajor)
  }

  LaunchedEffect(key1 = Unit) {
    viewModel.initData()
  }

  OpenMajorScreen(
    uiState = uiState,
    pagerState = pagerState,
    allOpenMajorListState = allOpenMajorListState,
    onClickClose = viewModel::popBackStack,
    onClickConfirmButton = viewModel::popBackStackWithArgument,
    onClickOpenMajorContainer = viewModel::updateSelectedOpenMajor,
    onClickTab = viewModel::syncPagerState,
    onValueChangeSearchBar = viewModel::updateSearchValue,
    onClickSearchBarClearButton = { viewModel.updateSearchValue("") },
  )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OpenMajorScreen(
  uiState: OpenMajorState = OpenMajorState(),
  pagerState: PagerState = rememberPagerState(pageCount = { OPEN_MAJOR_PAGE_COUNT }),
  allOpenMajorListState: LazyListState = rememberLazyListState(),
  onClickClose: () -> Unit = {},
  onClickConfirmButton: () -> Unit = {},
  onClickOpenMajorContainer: (String) -> Unit = {},
  onValueChangeSearchBar: (String) -> Unit = {},
  onClickSearchBarClearButton: () -> Unit = {},
  onClickTab: (Int) -> Unit = {},
) {
  Box(
    modifier = Modifier.background(White),
  ) {
    Column(
      modifier = Modifier.fillMaxSize(),
    ) {
      SuwikiAppBarWithTitle(
        showBackIcon = false,
        title = "개설학과",
        onClickClose = onClickClose,
      )

      SuwikiSearchBar(
        modifier = Modifier.padding(vertical = 10.dp),
        placeholder = stringResource(R.string.open_major_screen_search_bar_placeholder),
        value = uiState.searchValue,
        onClickClearButton = onClickSearchBarClearButton,
        onValueChange = onValueChangeSearchBar,
      )

      SuwikiTabBar(
        selectedTabPosition = pagerState.currentPage,
      ) {
        OpenMajorTap.entries.forEach { tab ->
          with(tab) {
            TabTitle(
              title = stringResource(id = titleId),
              position = position,
              selected = pagerState.currentPage == position,
              onClick = { onClickTab(position) },
            )
          }
        }
      }

      HorizontalPager(
        modifier = Modifier.weight(1f),
        state = pagerState,
      ) { page ->
        when (OpenMajorTap.entries[page]) {
          OpenMajorTap.ALL -> {
            if (uiState.showAllOpenMajorEmptySearchResultScreen) {
              EmptyText(stringResource(R.string.open_major_empty_search_result))
            } else {
              OpenMajorLazyColumn(
                listState = allOpenMajorListState,
                openMajorList = uiState.filteredAllOpenMajorList,
                onClickOpenMajorContainer = onClickOpenMajorContainer,
              )
            }
          }
        }
      }

      Box(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp),
      ) {
        SuwikiContainedLargeButton(
          modifier = Modifier
            .imePadding()
            .suwikiShadow(
              color = if (uiState.showBottomShadow) White else Color.Transparent,
              blurRadius = 80.dp,
              spread = 50.dp,
            ),
          text = stringResource(R.string.word_confirm),
          onClick = onClickConfirmButton,
        )
      }
    }

    if (uiState.isLoading) LoadingScreen()
  }
}

@Composable
private fun EmptyText(
  text: String = "",
) {
  Text(
    modifier = Modifier
      .padding(52.dp)
      .fillMaxSize(),
    textAlign = TextAlign.Center,
    text = text,
    style = SuwikiTheme.typography.header4,
    color = Gray95,
  )
}

@Composable
private fun OpenMajorLazyColumn(
  listState: LazyListState,
  openMajorList: PersistentList<OpenMajor>,
  onClickOpenMajorContainer: (String) -> Unit = {},
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    state = listState,
    contentPadding = PaddingValues(top = 12.dp),
  ) {
    items(
      items = openMajorList,
      key = { it.id },
    ) { openMajor ->
      with(openMajor) {
        OpenMajorContainer(
          text = name,
          isChecked = isSelected,
          onClick = { onClickOpenMajorContainer(name) },
        )
      }
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun OpenMajorScreenPreview() {
  SuwikiTheme {
    OpenMajorScreen()
  }
}
