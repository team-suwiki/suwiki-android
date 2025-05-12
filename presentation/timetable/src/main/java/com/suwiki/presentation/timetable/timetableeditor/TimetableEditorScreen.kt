package com.suwiki.presentation.timetable.timetableeditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suwiki.presentation.common.designsystem.component.appbar.SuwikiAppBarWithTitle
import com.suwiki.presentation.common.designsystem.component.bottomsheet.SuwikiSelectBottomSheet
import com.suwiki.presentation.common.designsystem.component.button.SuwikiContainedLargeButton
import com.suwiki.presentation.common.designsystem.component.container.SuwikiSelectionContainer
import com.suwiki.presentation.common.designsystem.component.textfield.SuwikiRegularTextField
import com.suwiki.presentation.common.designsystem.theme.SuwikiTheme
import com.suwiki.presentation.common.designsystem.theme.White
import com.suwiki.presentation.timetable.R
import kotlinx.collections.immutable.toPersistentList
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun TimetableEditorRoute(
  viewModel: TimetableEditorViewModel = koinViewModel(),
  popBackStack: () -> Unit,
  handleException: (Throwable) -> Unit,
  onShowToast: (String) -> Unit,
) {
  val uiState = viewModel.collectAsState().value
  val context = LocalContext.current
  viewModel.collectSideEffect { sideEffect ->
    when (sideEffect) {
      is TimetableEditorSideEffect.HandleException -> handleException(sideEffect.throwable)
      TimetableEditorSideEffect.PopBackStack -> popBackStack()
      TimetableEditorSideEffect.NeedSelectSemesterToast -> onShowToast(
        context.getString(R.string.create_timetable_need_select_semester),
      )
    }
  }
  TimetableEditorScreen(
    uiState = uiState,
    onValueChangeTimetableName = viewModel::updateName,
    onClickBack = viewModel::popBackStack,
    onClickCompleteButton = viewModel::upsertTimetable,
    onClickSelectionContainer = viewModel::showSemesterBottomSheet,
    hideSemesterBottomSheet = viewModel::hideSemesterBottomSheet,
    onClickSemesterItem = { position ->
      viewModel.hideSemesterBottomSheet()
      viewModel.updateSemesterPosition(position)
    },
    onClickTextFieldClearButton = { viewModel.updateName("") },
  )
}

@Composable
fun TimetableEditorScreen(
  uiState: TimetableEditorState = TimetableEditorState(),
  onValueChangeTimetableName: (String) -> Unit = {},
  onClickTextFieldClearButton: () -> Unit = {},
  onClickBack: () -> Unit = {},
  onClickCompleteButton: () -> Unit = {},
  onClickSelectionContainer: () -> Unit = {},
  hideSemesterBottomSheet: () -> Unit = {},
  onClickSemesterItem: (Int) -> Unit = {},
) {
  val context = LocalContext.current

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(White),
  ) {
    SuwikiAppBarWithTitle(
      showCloseIcon = false,
      onClickBack = onClickBack,
    )

    Spacer(modifier = Modifier.size(20.dp))

    Column(
      modifier = Modifier.padding(24.dp),
    ) {
      SuwikiSelectionContainer(
        title = uiState.semester?.toText() ?: stringResource(R.string.word_select_semester),
        onClick = onClickSelectionContainer,
      )

      Spacer(modifier = Modifier.size(22.dp))

      SuwikiRegularTextField(
        value = uiState.name,
        onValueChange = onValueChangeTimetableName,
        onClickClearButton = onClickTextFieldClearButton,
        placeholder = stringResource(R.string.create_timetable_screen_placeholder),
      )

      Spacer(modifier = Modifier.weight(1f))

      SuwikiContainedLargeButton(
        modifier = Modifier.imePadding(),
        text = stringResource(R.string.word_complete),
        enabled = uiState.buttonEnabled,
        clickable = uiState.buttonEnabled,
        onClick = onClickCompleteButton,
      )
    }
  }

  SuwikiSelectBottomSheet(
    isSheetOpen = uiState.isSheetOpenSemester,
    onDismissRequest = hideSemesterBottomSheet,
    onClickItem = { onClickSemesterItem(it) },
    itemList = semesterList.map { it.toText(context) }.toPersistentList(),
    title = stringResource(R.string.word_select_semester),
    selectedPosition = uiState.selectedSemesterPosition,
  )
}

@Preview
@Composable
fun TimetableEditorScreenPreview() {
  SuwikiTheme {
    TimetableEditorScreen()
  }
}
