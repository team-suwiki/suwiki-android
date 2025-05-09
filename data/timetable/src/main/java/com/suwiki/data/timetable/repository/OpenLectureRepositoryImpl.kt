package com.suwiki.data.timetable.repository

import com.suwiki.common.model.timetable.OpenLecture
import com.suwiki.data.timetable.TimetableUtil
import com.suwiki.data.timetable.datasource.LocalOpenLectureDataSource
import com.suwiki.data.timetable.datasource.RemoteOpenLectureDataSource
import com.suwiki.domain.timetable.repository.OpenLectureRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class OpenLectureRepositoryImpl @Inject constructor(
  private val remoteOpenLectureDataSource: RemoteOpenLectureDataSource,
  private val localOpenLectureDataSource: LocalOpenLectureDataSource,
) : OpenLectureRepository {
  override fun getOpenLectureList(
    lectureOrProfessorName: String?,


    major: String?,
    grade: Int?,
  ): Flow<List<OpenLecture>> {
    return localOpenLectureDataSource.getOpenLectureList(
      lectureOrProfessorName = lectureOrProfessorName,
      major = major,
      grade = grade,
    )
  }

  override suspend fun checkNeedUpdate(): Boolean {
    val localVersion = localOpenLectureDataSource.getOpenLectureListVersion().firstOrNull() ?: return true
    val remoteVersion = remoteOpenLectureDataSource.getOpenLectureListVersion()
    return remoteVersion > localVersion
  }

  override suspend fun updateAllLectures() = coroutineScope {
    val remoteOpenLectures = async {
      remoteOpenLectureDataSource.getOpenLectureList().map {
        OpenLecture(
          id = it.number,
          name = it.className,
          type = it.classification,
          major = it.major,
          grade = it.grade,
          professorName = it.professor,
          originalCellList = TimetableUtil.parseTimeTableString(it.time),
        )
      }
    }

    val remoteOpenLectureVersion = async {
      remoteOpenLectureDataSource.getOpenLectureListVersion()
    }

    localOpenLectureDataSource.updateAllLectures(remoteOpenLectures.await())
    localOpenLectureDataSource.setOpenLectureListVersion(remoteOpenLectureVersion.await())
  }

  override suspend fun getLastUpdatedDate(): String? {
    return try {
      val version = localOpenLectureDataSource.getOpenLectureListVersion().firstOrNull().toString()
      if (version.length != 12) {
        return null
      }

      val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
      val dateTime = LocalDateTime.parse(version, formatter)

      val koreanFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분", Locale.KOREAN)
      dateTime.format(koreanFormatter)
    } catch (e: Exception) {
      null
    }
  }

  override suspend fun getOpenMajor(): List<String> {
    return localOpenLectureDataSource.getOpenLectureList().map { list -> list.map { it.major } }.firstOrNull() ?: emptyList()
  }
}
