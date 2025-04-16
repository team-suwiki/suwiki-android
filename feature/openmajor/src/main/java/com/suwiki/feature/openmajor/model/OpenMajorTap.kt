package com.suwiki.feature.openmajor.model

import androidx.annotation.StringRes
import com.suwiki.feature.openmajor.R

enum class OpenMajorTap(
  val position: Int,
  @StringRes val titleId: Int,
) {
  ALL(
    position = 0,
    titleId = R.string.word_all,
  ),
}
