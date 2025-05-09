package com.suwiki.remote.timetable.datasource

import com.google.firebase.database.FirebaseDatabase
import com.suwiki.data.timetable.OpenLectureRaw
import com.suwiki.data.timetable.datasource.RemoteOpenLectureDataSource
import kotlinx.coroutines.tasks.await
import okhttp3.internal.toLongOrDefault
import javax.inject.Inject

class RemoteOpenLectureDataSourceImpl @Inject constructor(
  private val firebaseDatabase: FirebaseDatabase,
) : RemoteOpenLectureDataSource {

  override suspend fun getOpenLectureListVersion(): Long =
    firebaseDatabase
      .getReference(DATABASE_OPEN_LECTURE_VERSION)
      .get()
      .await()
      .value
      .toString()
      .toLongOrDefault(0)


  override suspend fun getOpenLectureList(): List<OpenLectureRaw> = firebaseDatabase
    .getReference(DATABASE_OPEN_LECTURE)
    .get()
    .await()
    .children
    .mapIndexed { index, dataSnapshot ->
      val data = dataSnapshot.value as HashMap<*, *>
      OpenLectureRaw(
        number = index.toLong() + 1,
        major = data[FIELD_MAJOR].toString(),
        grade = data[FIELD_GRADE].toString().toIntOrNull() ?: 1,
        className = data[FIELD_CLASS_NAME].toString(),
        classification = data[FIELD_CLASSIFICATION].toString(),
        professor = data[FIELD_PROFESSOR]?.toString() ?: DEFAULT,
        time = data[FIELD_TIME]?.toString() ?: DEFAULT,
      )
    }


  companion object {
    private const val FIELD_MAJOR = "major"
    private const val FIELD_GRADE = "grade"
    private const val FIELD_CLASS_NAME = "name"
    private const val FIELD_CLASSIFICATION = "divide"
    private const val FIELD_PROFESSOR = "profe"
    private const val FIELD_TIME = "time"

    private const val DEFAULT = "없음"

    private const val DATABASE_OPEN_LECTURE = "openLecture"
    private const val DATABASE_OPEN_LECTURE_VERSION = "openLectureVersion"
  }
}
