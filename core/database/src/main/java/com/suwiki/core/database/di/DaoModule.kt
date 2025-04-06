package com.suwiki.core.database.di

import com.suwiki.core.database.dao.OpenLectureDao
import com.suwiki.core.database.dao.OpenMajorDao
import com.suwiki.core.database.dao.TimeTableDao
import com.suwiki.core.database.database.OpenLectureDatabase
import com.suwiki.core.database.database.OpenMajorDatabase
import com.suwiki.core.database.database.TimetableDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

  @Provides
  fun provideOpenMajorDao(db: OpenMajorDatabase): OpenMajorDao = db.openMajorDao()

  @Provides
  fun provideTimetableDao(db: TimetableDatabase): TimeTableDao = db.timetableDao()

  @Provides
  fun provideOpenLectureDao(db: OpenLectureDatabase): OpenLectureDao = db.openLectureDao()
}
