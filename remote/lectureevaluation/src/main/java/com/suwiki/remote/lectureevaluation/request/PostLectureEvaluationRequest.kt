package com.suwiki.remote.lectureevaluation.request

import kotlinx.serialization.Serializable

@Serializable
data class PostLectureEvaluationRequest(
  val lectureName: String,
  val professor: String,
  val selectedSemester: String,
  val satisfaction: Float,
  val learning: Float,
  val honey: Float,
  val team: Int,
  val difficulty: Int,
  val homework: Int,
  val content: String,
)
