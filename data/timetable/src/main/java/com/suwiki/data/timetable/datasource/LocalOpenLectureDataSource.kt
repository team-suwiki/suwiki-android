package com.suwiki.data.timetable.datasource

import com.suwiki.common.model.timetable.OpenLecture
import kotlinx.coroutines.flow.Flow

interface LocalOpenLectureDataSource {
  fun getOpenLectureListVersion(): Flow<Long?>

  suspend fun setOpenLectureListVersion(version: Long)

  fun getOpenLectureList(
    lectureOrProfessorName: String? = null,
    major: String? = null,
    grade: Int? = null
  ): Flow<List<OpenLecture>>

  suspend fun updateAllLectures(lectures: List<OpenLecture>)
}
