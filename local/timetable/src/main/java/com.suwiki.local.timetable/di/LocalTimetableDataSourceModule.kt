package com.suwiki.local.timetable.di

import com.suwiki.data.timetable.datasource.LocalOpenLectureDataSource
import com.suwiki.data.timetable.datasource.LocalTimetableDataSource
import com.suwiki.local.timetable.datasource.LocalOpenLectureDatasourceImpl
import com.suwiki.local.timetable.datasource.LocalTimetableDatasourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val localTimetableDataSourceModule = module {
  single<LocalTimetableDataSource> {
    LocalTimetableDatasourceImpl(get(named("normalDataStore")), get())
  }

  single<LocalOpenLectureDataSource>{
    LocalOpenLectureDatasourceImpl(get(named("normalDataStore")), get())
  }
}
