package com.suwiki.domain.lectureevaluation.usecase.exam

import com.suwiki.common.model.lectureevaluation.PurchaseHistory
import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.lectureevaluation.repository.ExamMyRepository
import javax.inject.Inject

class GetPurchaseHistoryUseCase @Inject constructor(
  private val examMyRepository: ExamMyRepository,
) {
  suspend operator fun invoke(): Result<List<PurchaseHistory>> = runCatchingIgnoreCancelled {
    examMyRepository.getPurchaseHistory()
  }
}
