package com.suwiki.domain.timetable.usecase

import com.suwiki.common.model.timetable.Timetable
import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.TimetableRepository

class InsertTimetableUseCase(
  private val timetableRepository: TimetableRepository,
) {
  suspend operator fun invoke(param: Param): Result<Unit> = runCatchingIgnoreCancelled {
    val createTime = System.currentTimeMillis()
    with(timetableRepository) {
      insertTimetable(
        Timetable(
          createTime = createTime,
          year = param.year,
          semester = param.semester,
          name = param.name,
          cellList = emptyList(),
        ),
      )
      setMainTimetableCreateTime(createTime)
    }
  }

  data class Param(
    val name: String,
    val year: String,
    val semester: String,
  )
}
