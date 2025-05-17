package com.suwiki.domain.timetable.usecase

import com.suwiki.common.model.timetable.Timetable
import com.suwiki.common.model.timetable.TimetableCell
import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.TimetableRepository

class DeleteTimetableCellUseCase(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(cell: TimetableCell): Result<Timetable> = runCatchingIgnoreCancelled {
    timetableRepository.deleteTimetableCell(cell = cell)
  }
}
