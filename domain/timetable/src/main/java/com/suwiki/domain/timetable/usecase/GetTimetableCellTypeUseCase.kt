package com.suwiki.domain.timetable.usecase

import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.TimetableRepository

class GetTimetableCellTypeUseCase(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke() = runCatchingIgnoreCancelled {
    timetableRepository.getTimetableCellType()
  }
}
