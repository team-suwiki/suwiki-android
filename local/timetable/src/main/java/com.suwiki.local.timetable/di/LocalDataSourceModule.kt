package com.suwiki.local.timetable.di

import com.suwiki.data.timetable.datasource.LocalOpenLectureDatasource
import com.suwiki.data.timetable.datasource.LocalTimetableDataSource
import com.suwiki.local.timetable.datasource.LocalOpenLectureDatasourceImpl
import com.suwiki.local.timetable.datasource.LocalTimetableDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

  @Singleton
  @Binds
  abstract fun bindLocalOpenLectureDataSource(
    localOpenLectureDataSourceImpl: LocalOpenLectureDatasourceImpl,
  ): LocalOpenLectureDatasource

  @Singleton
  @Binds
  abstract fun bindLocalTimetableDataSource(
    localTimetableDatasourceImpl: LocalTimetableDatasourceImpl,
  ): LocalTimetableDataSource
}
