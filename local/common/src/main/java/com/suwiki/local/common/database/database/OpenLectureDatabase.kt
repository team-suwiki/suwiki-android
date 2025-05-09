package com.suwiki.local.common.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.suwiki.local.common.database.converter.OpenLectureConverter
import com.suwiki.local.common.database.dao.OpenLectureDao
import com.suwiki.local.common.database.entity.OpenLectureEntity


@Database(entities = [OpenLectureEntity::class], version = 1)
@TypeConverters(OpenLectureConverter::class)
abstract class OpenLectureDatabase : RoomDatabase() {
  abstract fun openLectureDao(): OpenLectureDao
}
