package com.suwiki.domain.lectureevaluation.usecase.lecture

import com.suwiki.domain.common.runCatchingIgnoreCancelled
import com.suwiki.domain.lectureevaluation.repository.LectureEditorRepository
import javax.inject.Inject

class PostLectureEvaluationUseCase @Inject constructor(
  private val lectureEditorRepository: LectureEditorRepository,
) {
  suspend operator fun invoke(param: Param): Result<Unit> = runCatchingIgnoreCancelled {
    param.run {
      lectureEditorRepository.postLectureEvaluation(
        lectureId = id,
        lectureName = lectureName,
        professor = professor,
        selectedSemester = selectedSemester,
        satisfaction = satisfaction,
        learning = learning,
        honey = honey,
        team = team,
        difficulty = difficulty,
        homework = homework,
        content = content,
      )
    }
  }

  data class Param(
    val id: Long,
    val lectureName: String,
    val professor: String,
    val selectedSemester: String,
    val satisfaction: Float,
    val learning: Float,
    val honey: Float,
    val team: Int,
    val difficulty: Int,
    val homework: Int,
    val content: String,
  )
}
