package com.suwiki.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.suwiki.core.database.model.OpenLectureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OpenLectureDao {

  @Query(
    """
        SELECT * FROM OpenLectureEntity
        WHERE (:lectureOrProfessorName IS NULL OR name LIKE '%' || :lectureOrProfessorName || '%' OR professorName LIKE '%' || :lectureOrProfessorName || '%')
        AND (:major IS NULL OR major LIKE '%' || :major || '%')
        AND (:grade IS NULL OR grade = :grade)
    """
  )
  fun searchLectures(
    lectureOrProfessorName: String? = null,
    major: String? = null,
    grade: Int? = null
  ): Flow<List<OpenLectureEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectures(lectures: List<OpenLectureEntity>)

    @Query("DELETE FROM OpenLectureEntity")
    suspend fun deleteAllLectures()

    @Transaction
    suspend fun updateAllLectures(lectures: List<OpenLectureEntity>) {
        deleteAllLectures()
        insertLectures(lectures)
    }
}
