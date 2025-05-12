package com.suwiki.presentation.timetable.celleditor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suwiki.common.model.timetable.TimetableCellColor
import com.suwiki.common.model.timetable.TimetableDay
import com.suwiki.presentation.common.designsystem.component.appbar.SuwikiAppBarWithTitle
import com.suwiki.presentation.common.designsystem.component.button.SuwikiContainedLargeButton
import com.suwiki.presentation.common.designsystem.component.button.SuwikiContainedSmallButton
import com.suwiki.presentation.common.designsystem.component.chip.SuwikiOutlinedChip
import com.suwiki.presentation.common.designsystem.component.textfield.SuwikiSmallTextField
import com.suwiki.presentation.common.designsystem.theme.Gray6A
import com.suwiki.presentation.common.designsystem.theme.SuwikiTheme
import com.suwiki.presentation.common.designsystem.theme.White
import com.suwiki.presentation.common.ui.extension.suwikiClickable
import com.suwiki.presentation.common.ui.util.timetableCellColorHexMap
import com.suwiki.presentation.timetable.R
import com.suwiki.presentation.timetable.timetable.component.timetable.toText
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CellEditorRoute(
  viewModel: CellEditorViewModel = koinViewModel(),
  popBackStack: () -> Unit,
  handleException: (Throwable) -> Unit,
  onShowToast: (String) -> Unit,
) {
  val uiState = viewModel.collectAsState().value
  val context = LocalContext.current
  viewModel.collectSideEffect { sideEffect ->
    when (sideEffect) {
      is CellEditorSideEffect.HandleException -> handleException(sideEffect.throwable)
      CellEditorSideEffect.PopBackStack -> popBackStack()
      is CellEditorSideEffect.ShowToast -> onShowToast(sideEffect.msg)
      CellEditorSideEffect.ShowAddSuccessCellToast -> onShowToast(context.getString(R.string.open_lecture_success_add_cell_toast))
      CellEditorSideEffect.ShowNeedLectureNameToast -> onShowToast(context.getString(R.string.add_cell_screen_need_lecture_name))
      CellEditorSideEffect.ShowNeedLocationToast -> onShowToast(context.getString(R.string.add_cell_screen_need_location))
      CellEditorSideEffect.ShowNeedProfessorNameToast -> onShowToast(context.getString(R.string.add_cell_screen_need_professor_name))
      CellEditorSideEffect.ShowEditSuccessCellToast -> onShowToast(context.getString(R.string.open_lecture_success_edit_cell_toast))
    }
  }
  CellEditorScreen(
    uiState = uiState,
    onClickClose = viewModel::popBackStack,
    onValueChangeLectureName = viewModel::updateLectureName,
    onValueChangeProfessorName = viewModel::updateProfessorName,
    onClickDayChip = viewModel::updateCellDay,
    onValueChangeStartPeriod = viewModel::updateCellStartPeriod,
    onValueChangeEndPeriod = viewModel::updateCellEndPeriod,
    onValueChangeLocation = viewModel::updateCellLocation,
    onClickAddButton = viewModel::addCell,
    onClickDeleteButton = viewModel::deleteCell,
    onClickColorChip = viewModel::updateCellColor,
    onClickCompleteButton = viewModel::upsertTimetable,
  )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CellEditorScreen(
  uiState: CellEditorState = CellEditorState(),
  onClickClose: () -> Unit = {},
  onValueChangeLectureName: (String) -> Unit = {},
  onValueChangeProfessorName: (String) -> Unit = {},
  onClickDayChip: (Int, TimetableDay) -> Unit = { _, _ -> },
  onValueChangeStartPeriod: (Int, String) -> Unit = { _, _ -> },
  onValueChangeEndPeriod: (Int, String) -> Unit = { _, _ -> },
  onValueChangeLocation: (Int, String) -> Unit = { _, _ -> },
  onClickAddButton: () -> Unit = {},
  onClickDeleteButton: (Int) -> Unit = {},
  onClickColorChip: (TimetableCellColor) -> Unit = {},
  onClickCompleteButton: () -> Unit = {},
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(White),
  ) {
    SuwikiAppBarWithTitle(
      showBackIcon = false,
      title = stringResource(R.string.add_cell_screen_title),
      onClickClose = onClickClose,
    )

    Column(
      modifier = Modifier
        .weight(1f)
        .verticalScroll(rememberScrollState()),
    ) {
      Spacer(modifier = Modifier.size(20.dp))

      EditorScreenRow(
        name = stringResource(R.string.add_cell_screen_lecture_name),
        verticalAlignment = Alignment.CenterVertically,
        content = {
          SuwikiSmallTextField(
            showClearButton = false,
            value = uiState.lectureName,
            onValueChange = onValueChangeLectureName,
            placeholder = stringResource(R.string.add_cell_screen_input_lecture_name),
          )
        },
      )

      EditorScreenRow(
        name = stringResource(R.string.add_cell_screen_professor_name),
        verticalAlignment = Alignment.CenterVertically,
        content = {
          SuwikiSmallTextField(
            showClearButton = false,
            value = uiState.professorName,
            onValueChange = onValueChangeProfessorName,
            placeholder = stringResource(R.string.add_cell_screen_input_professor_name),
          )
        },
      )

      EditorScreenRow(
        name = stringResource(R.string.add_cell_screen_time_location),
        verticalAlignment = Alignment.CenterVertically,
        content = {
          Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            SuwikiContainedSmallButton(
              text = stringResource(R.string.word_add),
              onClick = onClickAddButton,
            )
          }
        },
      )

      uiState.cellStateList.forEachIndexed { index, cell ->
        Column {
          EditorScreenRow(
            name = stringResource(R.string.add_cell_screen_day_of_week),
            verticalAlignment = Alignment.Top,
            content = {
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
              ) {
                FlowRow(
                  modifier = Modifier.weight(1f, false),
                  verticalArrangement = Arrangement.spacedBy(6.dp),
                  horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                  TimetableDay.entries.filter { it != TimetableDay.E_LEARNING }.forEach { day ->
                    SuwikiOutlinedChip(
                      text = day.toText(),
                      isChecked = cell.day == day,
                      onClick = { onClickDayChip(index, day) },
                    )
                  }
                }
                Spacer(modifier = Modifier.size(10.dp))
                SuwikiContainedSmallButton(
                  text = stringResource(R.string.word_delete),
                  onClick = { onClickDeleteButton(index) },
                )
              }
            },
          )

          Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
              SuwikiSmallTextField(
                value = cell.startPeriod,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { onValueChangeStartPeriod(index, it) },
                showClearButton = false,
                textStyle = SuwikiTheme.typography.body5.copy(textAlign = TextAlign.Center),
                modifier = Modifier.width(27.dp),
                placeholder = stringResource(R.string.add_cell_screen_period),
              )

              HorizontalDivider(
                modifier = Modifier.width(14.dp),
              )

              SuwikiSmallTextField(
                value = cell.endPeriod,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = { onValueChangeEndPeriod(index, it) },
                showClearButton = false,
                textStyle = SuwikiTheme.typography.body5.copy(textAlign = TextAlign.Center),
                modifier = Modifier.width(27.dp),
                placeholder = stringResource(R.string.add_cell_screen_period),
              )
            }

            SuwikiSmallTextField(
              showClearButton = false,
              value = cell.location,
              onValueChange = { onValueChangeLocation(index, it) },
              placeholder = stringResource(R.string.add_cell_screen_input_location),
            )
          }
        }
      }

      Spacer(modifier = Modifier.size(20.dp))

      EditorScreenRow(
        name = stringResource(R.string.add_cell_screen_color),
        verticalAlignment = Alignment.Top,
        content = {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
          ) {
            FlowRow(
              modifier = Modifier.weight(1f, false),
              verticalArrangement = Arrangement.spacedBy(12.dp),
              horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
              TimetableCellColor.entries.forEach {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color(timetableCellColorHexMap[it]!!))
                    .suwikiClickable(
                      rippleEnabled = false,
                      onClick = { onClickColorChip(it) },
                    ),
                  contentAlignment = Alignment.Center,
                ) {
                  if (it == uiState.selectedTimetableCellColor) {
                    Icon(
                      modifier = Modifier.size(16.dp),
                      painter = painterResource(id = R.drawable.ic_align_checked),
                      contentDescription = null,
                      tint = White,
                    )
                  }
                }
              }
            }
          }
        },
      )
    }

    SuwikiContainedLargeButton(
      modifier = Modifier
        .padding(horizontal = 24.dp)
        .imePadding(),
      text = stringResource(id = R.string.word_complete),
      onClick = onClickCompleteButton,
    )
    Spacer(modifier = Modifier.size(20.dp))
  }
}

@Composable
fun EditorScreenRow(
  modifier: Modifier = Modifier,
  name: String,
  verticalAlignment: Alignment.Vertical,
  content: @Composable () -> Unit,
) {
  Row(
    modifier = modifier.padding(horizontal = 24.dp, vertical = 12.dp),
    verticalAlignment = verticalAlignment,
  ) {
    Text(
      modifier = Modifier.widthIn(min = 60.dp),
      text = name,
      style = SuwikiTheme.typography.body4,
      color = Gray6A,
    )

    content()
  }
}

@Preview
@Composable
fun CellEditorScreenPreview() {
  SuwikiTheme {
    CellEditorScreen()
  }
}
