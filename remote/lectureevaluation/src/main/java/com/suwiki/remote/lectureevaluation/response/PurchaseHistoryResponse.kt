package com.suwiki.remote.lectureevaluation.response

import com.suwiki.common.model.lectureevaluation.PurchaseHistory
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class PurchaseHistoryResponse(
  val id: Long,
  val lectureName: String,
  val professor: String,
  val majorType: String,
  val createDate: LocalDateTime,
)

internal fun PurchaseHistoryResponse.toModel() = PurchaseHistory(
  id = id,
  lectureName = lectureName,
  professor = professor,
  majorType = majorType,
  createDate = createDate.toJavaLocalDateTime(),
)
