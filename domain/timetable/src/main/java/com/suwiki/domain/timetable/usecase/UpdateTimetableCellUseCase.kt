package com.suwiki.domain.timetable.usecase

import com.suwiki.common.model.timetable.TimetableCell
import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.TimetableRepository

class UpdateTimetableCellUseCase(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(param: Param): Result<Unit> = runCatchingIgnoreCancelled {
    timetableRepository.updateTimetableCell(oldCellId = param.oldCellId, cellList = param.cellList)
  }

  data class Param(
    val oldCellId: String,
    val cellList: List<TimetableCell>,
  )
}
