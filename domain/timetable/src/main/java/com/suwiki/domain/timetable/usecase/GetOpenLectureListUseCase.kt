package com.suwiki.domain.timetable.usecase

import com.suwiki.domain.timetable.repository.OpenLectureRepository

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
