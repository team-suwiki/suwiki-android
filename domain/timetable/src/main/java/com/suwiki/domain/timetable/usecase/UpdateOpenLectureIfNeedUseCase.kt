package com.suwiki.domain.timetable.usecase

import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.OpenLectureRepository

class UpdateOpenLectureIfNeedUseCase(
  private val openLectureRepository: OpenLectureRepository,
) {
  suspend operator fun invoke(): Result<Unit> = runCatchingIgnoreCancelled {
    if(openLectureRepository.checkNeedUpdate().not()) return@runCatchingIgnoreCancelled
    openLectureRepository.updateAllLectures()
  }
}
