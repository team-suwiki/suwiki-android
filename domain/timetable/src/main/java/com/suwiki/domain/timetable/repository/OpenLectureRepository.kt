package com.suwiki.domain.timetable.repository

import com.suwiki.core.model.timetable.OpenLecture
import kotlinx.coroutines.flow.Flow

interface OpenLectureRepository {
  fun getOpenLectureList(
  lectureOrProfessorName: String? = null,
  major: String? = null,
  grade: Int? = null,
  ): Flow<List<OpenLecture>>

  suspend fun checkNeedUpdate(): Boolean

  suspend fun updateAllLectures()

  suspend fun getLastUpdatedDate(): String?

  suspend fun getOpenMajor(): List<String>
}
