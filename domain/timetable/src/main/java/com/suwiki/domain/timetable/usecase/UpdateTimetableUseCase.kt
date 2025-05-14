package com.suwiki.domain.timetable.usecase

import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.TimetableRepository

class UpdateTimetableUseCase(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(param: Param): Result<Unit> = runCatchingIgnoreCancelled {
    with(param) {
      timetableRepository.updateTimetable(
        createTime = createTime,
        year = year,
        semester = semester,
        name = name,
      )
    }
  }

  data class Param(
    val createTime: Long,
    val year: String,
    val semester: String,
    val name: String,
  )
}
