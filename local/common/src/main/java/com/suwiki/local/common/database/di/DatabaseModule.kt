package com.suwiki.local.common.database.di

import android.content.Context
import androidx.room.Room
import com.suwiki.local.common.database.database.OpenLectureDatabase
import com.suwiki.local.common.database.database.OpenMajorDatabase
import com.suwiki.local.common.database.database.TimetableDatabase
import com.suwiki.local.common.database.migration.TIMETABLE_MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Singleton
  @Provides
  fun provideOpenMajorDatabase(
    @ApplicationContext context: Context,
  ): OpenMajorDatabase {
    return Room
      .databaseBuilder(
        context,
        OpenMajorDatabase::class.java,
        DatabaseName.OPEN_MAJOR,
      )
      .fallbackToDestructiveMigration()
      .build()
  }

  @Singleton
  @Provides
  fun provideTimetableDatabase(
    @ApplicationContext context: Context,
  ): TimetableDatabase {
    return Room
      .databaseBuilder(
        context,
        TimetableDatabase::class.java,
        DatabaseName.TIMETABLE,
      )
      .addMigrations(TIMETABLE_MIGRATION_1_2)
      .fallbackToDestructiveMigration()
      .build()
  }

  @Provides
  @Singleton
  fun provideOpenLectureDatabase(@ApplicationContext context: Context): OpenLectureDatabase {
    return Room.databaseBuilder(
      context,
      OpenLectureDatabase::class.java,
      DatabaseName.OPEN_LECTURE
    ).build()
  }
}

object DatabaseName {
  const val OPEN_MAJOR = "open-major-database"
  const val TIMETABLE = "timetable-list-database"
  const val OPEN_LECTURE = "open-lecture-database"
}
