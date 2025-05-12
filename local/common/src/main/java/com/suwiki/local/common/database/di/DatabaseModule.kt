package com.suwiki.local.common.database.di

import androidx.room.Room
import com.suwiki.local.common.database.database.OpenLectureDatabase
import com.suwiki.local.common.database.database.OpenMajorDatabase
import com.suwiki.local.common.database.database.TimetableDatabase
import com.suwiki.local.common.database.migration.TIMETABLE_MIGRATION_1_2
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
  single {
    Room.databaseBuilder(
      androidContext(),
      OpenMajorDatabase::class.java,
      DatabaseName.OPEN_MAJOR,
    )
      .fallbackToDestructiveMigration()
      .build()
  }

  single {
    Room.databaseBuilder(
      androidContext(),
      TimetableDatabase::class.java,
      DatabaseName.TIMETABLE,
    )
      .addMigrations(TIMETABLE_MIGRATION_1_2)
      .fallbackToDestructiveMigration()
      .build()
  }

  single {
    Room.databaseBuilder(
      androidContext(),
      OpenLectureDatabase::class.java,
      DatabaseName.OPEN_LECTURE,
    ).build()
  }
}

object DatabaseName {
  const val OPEN_MAJOR = "open-major-database"
  const val TIMETABLE = "timetable-list-database"
  const val OPEN_LECTURE = "open-lecture-database"
}
