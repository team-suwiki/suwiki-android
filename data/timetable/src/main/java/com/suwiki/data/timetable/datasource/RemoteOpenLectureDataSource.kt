package com.suwiki.data.timetable.datasource

import com.suwiki.data.timetable.OpenLectureRaw

interface RemoteOpenLectureDataSource {
  suspend fun getOpenLectureListVersion(): Long

  suspend fun getOpenLectureList(): List<OpenLectureRaw>
}
