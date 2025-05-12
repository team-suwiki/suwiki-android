package com.suwiki.domain.timetable.usecase

import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.TimetableRepository
import javax.inject.Inject

class SetMainTimetableCreateTime(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(createTime: Long): Result<Unit> = runCatchingIgnoreCancelled {
    timetableRepository.setMainTimetableCreateTime(createTime)
  }
}
