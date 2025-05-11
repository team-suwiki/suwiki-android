package com.suwiki.local.common.database.di

import com.suwiki.local.common.database.dao.OpenLectureDao
import com.suwiki.local.common.database.database.OpenMajorDatabase
import com.suwiki.local.common.database.database.TimetableDatabase
import com.suwiki.local.common.database.dao.OpenMajorDao
import com.suwiki.local.common.database.dao.TimeTableDao
import com.suwiki.local.common.database.database.OpenLectureDatabase
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
