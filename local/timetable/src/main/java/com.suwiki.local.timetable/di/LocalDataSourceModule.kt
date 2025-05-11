package com.suwiki.local.timetable.di

import com.suwiki.data.timetable.datasource.LocalOpenLectureDataSource
import com.suwiki.data.timetable.datasource.LocalTimetableDataSource
import com.suwiki.data.timetable.di.timetableRepositoryModule
import com.suwiki.local.common.database.di.databaseModule
import com.suwiki.local.common.datastore.di.dataStoreModule
import com.suwiki.local.timetable.datasource.LocalOpenLectureDatasourceImpl
import com.suwiki.local.timetable.datasource.LocalTimetableDatasourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val localTimetableDataSourceModule = module {
  includes(dataStoreModule, databaseModule, timetableRepositoryModule)
  single {
    LocalTimetableDatasourceImpl(get(named("normalDataStore")), get())
  } bind LocalTimetableDataSource::class

  single {
    LocalOpenLectureDatasourceImpl(get(named("normalDataStore")), get())
  } bind LocalOpenLectureDataSource::class
}
