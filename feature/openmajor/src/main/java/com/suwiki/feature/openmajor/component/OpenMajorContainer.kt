package com.suwiki.feature.openmajor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.suwiki.core.designsystem.theme.Black
import com.suwiki.core.designsystem.theme.Blue5
import com.suwiki.core.designsystem.theme.GrayDA
import com.suwiki.core.designsystem.theme.Primary
import com.suwiki.core.designsystem.theme.SuwikiTheme
import com.suwiki.core.designsystem.theme.White
import com.suwiki.core.ui.extension.suwikiClickable
import com.suwiki.feature.openmajor.R

@Composable
fun OpenMajorContainer(
  modifier: Modifier = Modifier,
  text: String,
  isChecked: Boolean,
  isStared: Boolean,
  onClick: () -> Unit = {},
  onClickStar: () -> Unit = {},
) {
  val (textColor, backgroundColor) = if (isChecked) {
    Primary to Blue5
  } else {
    Black to White
  }

  Row(
    modifier = modifier
      .background(backgroundColor)
      .padding(vertical = 12.dp, horizontal = 24.dp)
      .fillMaxWidth()
      .suwikiClickable(
        rippleEnabled = false,
        onClick = onClick,
      ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start,
  ) {
    Text(
      text = text,
      style = SuwikiTheme.typography.body2,
      color = textColor,
    )
  }
}

@Preview(widthDp = 300, heightDp = 50)
@Composable
fun OpenMajorContainerPreview() {
  var isChecked by remember { mutableStateOf(false) }
  var isStared by remember { mutableStateOf(false) }

  SuwikiTheme {
    OpenMajorContainer(
      text = "개설학과명",
      isChecked = isChecked,
      isStared = isStared,
      onClick = { isChecked = !isChecked },
      onClickStar = { isStared = !isStared },
    )
  }
}
