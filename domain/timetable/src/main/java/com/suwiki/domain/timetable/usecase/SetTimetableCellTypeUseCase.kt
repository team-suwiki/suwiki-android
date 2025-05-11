package com.suwiki.domain.timetable.usecase

import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.TimetableRepository
import javax.inject.Inject

class SetTimetableCellTypeUseCase(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(type: String) = runCatchingIgnoreCancelled {
    timetableRepository.setTimetableCellType(type)
  }
}
