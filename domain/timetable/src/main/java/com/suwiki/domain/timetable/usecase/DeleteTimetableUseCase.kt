package com.suwiki.domain.timetable.usecase

import com.suwiki.common.model.timetable.Timetable
import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.TimetableRepository
import javax.inject.Inject

class DeleteTimetableUseCase(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(timetable: Timetable): Result<Unit> = runCatchingIgnoreCancelled {
    with(timetableRepository) {
      deleteTimetable(timetable)

      val mainTimetableCreateTime = getMainTimetableCreateTime()
      if (mainTimetableCreateTime != timetable.createTime) return@runCatchingIgnoreCancelled

      val firstTimetableCreateTime = getAllTimetable().firstOrNull()?.createTime ?: 0L
      setMainTimetableCreateTime(firstTimetableCreateTime)
    }
  }
}
