package com.suwiki.domain.timetable.usecase

import com.suwiki.common.model.timetable.Timetable
import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.TimetableRepository
import javax.inject.Inject

class GetAllTimetableUseCase(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(): Result<List<Timetable>> = runCatchingIgnoreCancelled {
    timetableRepository.getAllTimetable()
  }
}
