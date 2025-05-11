package com.suwiki.domain.timetable.usecase

import com.suwiki.common.model.timetable.OpenLectureData
import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.OpenLectureRepository
import javax.inject.Inject

class GetOpenLectureListUseCase(
  private val openLectureRepository: OpenLectureRepository,
) {
  operator fun invoke(param: Param) =
    with(param) {
      openLectureRepository
        .getOpenLectureList(
          lectureOrProfessorName = lectureOrProfessorName,
          major = major,
          grade = grade,
        )
    }

  data class Param(
    val lectureOrProfessorName: String?,
    val major: String?,
    val grade: Int?,
  )
}
