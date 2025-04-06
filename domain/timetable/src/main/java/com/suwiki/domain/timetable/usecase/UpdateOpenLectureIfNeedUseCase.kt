package com.suwiki.domain.timetable.usecase

import com.suwiki.core.common.runCatchingIgnoreCancelled
import com.suwiki.domain.timetable.repository.OpenLectureRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UpdateOpenLectureIfNeedUseCase @Inject constructor(
  private val openLectureRepository: OpenLectureRepository,
) {
  suspend operator fun invoke(): Result<Unit> = runCatchingIgnoreCancelled {
    if(openLectureRepository.checkNeedUpdate().not()) return@runCatchingIgnoreCancelled
    openLectureRepository.updateAllLectures()
  }
}
