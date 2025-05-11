package com.suwiki.presentation.timetable.openlecture

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suwiki.common.model.timetable.OpenLecture
import com.suwiki.common.model.timetable.TimetableCellColor
import com.suwiki.presentation.common.designsystem.component.appbar.SuwikiAppBarWithTextButton
import com.suwiki.presentation.common.designsystem.component.bottomsheet.SuwikiBottomSheet
import com.suwiki.presentation.common.designsystem.component.bottomsheet.SuwikiSelectBottomSheet
import com.suwiki.presentation.common.designsystem.component.button.SuwikiContainedLargeButton
import com.suwiki.presentation.common.designsystem.component.loading.LoadingScreen
import com.suwiki.presentation.common.designsystem.component.searchbar.SuwikiSearchBar
import com.suwiki.presentation.common.designsystem.theme.Gray6A
import com.suwiki.presentation.common.designsystem.theme.Gray95
import com.suwiki.presentation.common.designsystem.theme.GrayF6
import com.suwiki.presentation.common.designsystem.theme.Primary
import com.suwiki.presentation.common.designsystem.theme.SuwikiTheme
import com.suwiki.presentation.common.designsystem.theme.White
import com.suwiki.presentation.common.ui.extension.OnBottomReached
import com.suwiki.presentation.common.ui.extension.suwikiClickable
import com.suwiki.presentation.common.ui.util.timetableCellColorHexMap
import com.suwiki.presentation.timetable.R
import com.suwiki.presentation.timetable.navigation.argument.CellEditorArgument
import com.suwiki.presentation.timetable.openlecture.component.OpenLectureCard
import com.suwiki.presentation.timetable.openlecture.model.SchoolLevel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun OpenLectureRoute(
  viewModel: OpenLectureViewModel = koinViewModel(),
  selectedOpenMajor: String,
  popBackStack: () -> Unit,
  handleException: (Throwable) -> Unit,
  onShowToast: (String) -> Unit,
  navigateOpenMajor: (String) -> Unit,
  navigateCellEditor: (CellEditorArgument) -> Unit,
) {
  val uiState = viewModel.collectAsState().value

  val listState = rememberLazyListState()
  val scope = rememberCoroutineScope()

  val context = LocalContext.current

  viewModel.collectSideEffect { sideEffect ->
    when (sideEffect) {
      is OpenLectureSideEffect.HandleException -> handleException(sideEffect.throwable)
      OpenLectureSideEffect.NavigateAddCustomTimetableCell -> navigateCellEditor(CellEditorArgument())
      is OpenLectureSideEffect.NavigateOpenMajor -> navigateOpenMajor(sideEffect.selectedOpenMajor)
      OpenLectureSideEffect.PopBackStack -> popBackStack()
      OpenLectureSideEffect.ScrollToTop -> scope.launch {
        awaitFrame()
        listState.animateScrollToItem(0)
      }

      is OpenLectureSideEffect.ShowOverlapCellToast -> onShowToast(sideEffect.msg)
      OpenLectureSideEffect.ShowSuccessAddCellToast -> onShowToast(context.getString(R.string.open_lecture_success_add_cell_toast))
      is OpenLectureSideEffect.NavigateCellEditor -> navigateCellEditor(sideEffect.argument)
    }
  }

  LaunchedEffect(selectedOpenMajor) {
    viewModel.updateSelectedOpenMajor(selectedOpenMajor)
  }

  LaunchedEffect(key1 = viewModel) {
    viewModel.initData()
  }

  OpenLectureScreen(
    uiState = uiState,
    listState = listState,
    onClickOpenMajorFilterContainer = navigateOpenMajor,
    onDismissSchoolLevelBottomSheet = viewModel::hideGradeBottomSheet,
    onClickSchoolLevelFilterContainer = viewModel::showGradeBottomSheet,
    onClickSchoolLevelBottomSheetItem = { position ->
      viewModel.hideGradeBottomSheet()
      viewModel.updateSchoolLevelPosition(SchoolLevel.entries[position])
    },
    onClickBack = viewModel::popBackStack,
    onClickSearchButton = viewModel::searchOpenLecture,
    onClickClearButton = { viewModel.updateSearchValue("") },
    onValueChangeSearch = viewModel::updateSearchValue,
    onClickCellAdd = viewModel::showSelectColorBottomSheet,
    onClickApply = {
      viewModel.hideSelectColorBottomSheet()
      viewModel.insertTimetable()
    },
    onClickColorChip = viewModel::updateSelectedCellColor,
    onDismissColorSelectBottomSheet = viewModel::hideSelectColorBottomSheet,
    onClickClassInfoCard = viewModel::navigateCellEditor,
    onClickCustomAdd = viewModel::navigateAddCustomCell,
  )
}

@Composable
fun OpenLectureScreen(
  uiState: OpenLectureState = OpenLectureState(),
  listState: LazyListState = rememberLazyListState(),
  onClickOpenMajorFilterContainer: (String) -> Unit = {},
  onDismissSchoolLevelBottomSheet: () -> Unit = {},
  onClickSchoolLevelFilterContainer: () -> Unit = {},
  onClickSchoolLevelBottomSheetItem: (Int) -> Unit = {},
  onClickBack: () -> Unit = {},
  onClickSearchButton: (String) -> Unit = {},
  onClickClearButton: () -> Unit = {},
  onValueChangeSearch: (String) -> Unit = {},
  onClickCellAdd: (OpenLecture) -> Unit = {},
  onClickApply: () -> Unit = {},
  onDismissColorSelectBottomSheet: () -> Unit = {},
  onClickColorChip: (TimetableCellColor) -> Unit = {},
  onClickClassInfoCard: (OpenLecture) -> Unit = {},
  onClickCustomAdd: () -> Unit = {},
) {
  val context = LocalContext.current

  val state = rememberCollapsingToolbarScaffoldState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(White),
  ) {
    SuwikiAppBarWithTextButton(
      buttonText = stringResource(R.string.add_timetable_screen_add_lecture),
      onClickBack = onClickBack,
      onClickTextButton = onClickCustomAdd,
    )

    CollapsingToolbarScaffold(
      modifier = Modifier
        .weight(1f),
      state = state,
      scrollStrategy = ScrollStrategy.EnterAlways,
      toolbar = {
        Spacer(
          modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
        )
        Column(
          modifier = Modifier.graphicsLayer {
            alpha = state.toolbarState.progress
          },
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
          ) {
            FilterContainer(
              filterName = stringResource(R.string.word_open_major),
              value = uiState.selectedOpenMajor,
              onClick = { onClickOpenMajorFilterContainer(uiState.selectedOpenMajor) },
            )

            FilterContainer(
              filterName = stringResource(R.string.word_school_level),
              value = stringResource(id = uiState.schoolLevel.stringResId),
              onClick = onClickSchoolLevelFilterContainer,
            )
          }

          SuwikiSearchBar(
            modifier = Modifier.padding(top = 10.dp),
            placeholder = stringResource(R.string.add_timetable_cell_search_bar_placeholder),
            onClickSearchButton = onClickSearchButton,
            value = uiState.searchValue,
            onClickClearButton = onClickClearButton,
            onValueChange = onValueChangeSearch,
          )

          Text(
            modifier = Modifier
              .padding(top = 10.dp, end = 20.dp)
              .align(Alignment.End),
            text = "최근 갱신일: ${uiState.lastUpdatedDate ?: "확인 중"}",
            style = SuwikiTheme.typography.body7,
            color = Gray95,
          )
        }
      },
    ) {
      if (uiState.openLectureList.isEmpty() && uiState.isLoading.not()) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {
          Spacer(modifier = Modifier.size(52.dp))

          Text(
            text = stringResource(R.string.open_lecture_screen_empty_result_title),
            style = SuwikiTheme.typography.header4,
            color = Gray95,
          )

          Spacer(modifier = Modifier.size(12.dp))

          Text(
            text = stringResource(R.string.open_lecture_screen_empty_result_description),
            style = SuwikiTheme.typography.body5,
            textAlign = TextAlign.Center,
            color = Gray95,
          )
        }
      }

      LazyColumn(
        modifier = Modifier
          .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        state = listState,
      ) {
        items(
          items = uiState.openLectureList,
          key = { it.id },
        ) { lectureEvaluation ->
          with(lectureEvaluation) {
            OpenLectureCard(
              className = name,
              professor = professorName,
              cellInfo = originalCellList.toText(context),
              grade = stringResource(id = R.string.word_num_school_level, grade),
              classType = type,
              openMajor = major,
              onClick = { onClickClassInfoCard(this) },
              onClickAdd = { onClickCellAdd(this) },
            )
          }
        }
      }
    }
  }

  ColorSelectBottomSheet(
    isSheetOpen = uiState.showSelectCellColorBottomSheet,
    selectedTimetableCellColor = uiState.selectedTimetableCellColor,
    onClickApply = onClickApply,
    onClickColorChip = onClickColorChip,
    onDismissColorSelectBottomSheet = onDismissColorSelectBottomSheet,
  )

  if (uiState.isLoading) {
    LoadingScreen()
  }

  SuwikiSelectBottomSheet(
    isSheetOpen = uiState.showSchoolLevelBottomSheet,
    onDismissRequest = onDismissSchoolLevelBottomSheet,
    onClickItem = onClickSchoolLevelBottomSheetItem,
    itemList = SchoolLevel.entries.map { stringResource(id = it.stringResId) }.toPersistentList(),
    title = stringResource(R.string.word_school_level),
    selectedPosition = uiState.schoolLevel.ordinal,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorSelectBottomSheet(
  isSheetOpen: Boolean,
  onDismissColorSelectBottomSheet: () -> Unit,
  selectedTimetableCellColor: TimetableCellColor,
  onClickColorChip: (TimetableCellColor) -> Unit,
  onClickApply: () -> Unit,
) {
  SuwikiBottomSheet(
    isSheetOpen = isSheetOpen,
    onDismissRequest = onDismissColorSelectBottomSheet,
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
      verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
      LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
      ) {
        items(items = TimetableCellColor.entries) {
          Box(
            modifier = Modifier
              .aspectRatio(1f)
              .clip(CircleShape)
              .background(Color(timetableCellColorHexMap[it]!!))
              .suwikiClickable(
                rippleEnabled = false,
                onClick = { onClickColorChip(it) },
              ),
            contentAlignment = Alignment.Center,
          ) {
            if (selectedTimetableCellColor == it) {
              Icon(
                painter = painterResource(id = R.drawable.ic_align_checked),
                contentDescription = null,
                tint = White,
              )
            }
          }
        }
      }

      SuwikiContainedLargeButton(
        text = stringResource(R.string.word_apply),
        onClick = onClickApply,
      )
    }
  }
}

@Composable
private fun FilterContainer(
  filterName: String,
  value: String,
  onClick: () -> Unit,
) {
  Row(
    modifier = Modifier
      .clip(RoundedCornerShape(size = 10.dp))
      .border(width = 1.dp, color = GrayF6, shape = RoundedCornerShape(size = 10.dp))
      .suwikiClickable(onClick = onClick)
      .padding(vertical = 6.dp, horizontal = 9.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(text = filterName, style = SuwikiTheme.typography.body6, color = Gray6A)
    Spacer(modifier = Modifier.size(4.dp))
    Text(text = value, style = SuwikiTheme.typography.body6, color = Primary)
    Icon(
      painter = painterResource(id = R.drawable.ic_dropdown_arrow_down),
      contentDescription = null,
      tint = Gray6A,
    )
  }
}

@Preview
@Composable
fun OpenLectureScreenPreview() {
  SuwikiTheme {
    OpenLectureScreen()
  }
}
