package com.suwiki.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.suwiki.core.database.dao.OpenLectureDao
import com.suwiki.core.database.database.converter.OpenLectureConverter
import com.suwiki.core.database.model.OpenLectureEntity


@Database(entities = [OpenLectureEntity::class], version = 1)
@TypeConverters(OpenLectureConverter::class)
abstract class OpenLectureDatabase : RoomDatabase() {
  abstract fun openLectureDao(): OpenLectureDao
}
