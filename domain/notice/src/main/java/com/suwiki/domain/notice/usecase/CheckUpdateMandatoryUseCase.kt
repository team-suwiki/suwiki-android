package com.suwiki.domain.notice.usecase

import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.notice.repository.NoticeRepository
import javax.inject.Inject

class CheckUpdateMandatoryUseCase @Inject constructor(
  private val noticeRepository: NoticeRepository,
) {
  suspend operator fun invoke(versionCode: Long): Result<Boolean> = runCatchingIgnoreCancelled {
    noticeRepository.checkUpdateMandatory(versionCode)
  }
}
