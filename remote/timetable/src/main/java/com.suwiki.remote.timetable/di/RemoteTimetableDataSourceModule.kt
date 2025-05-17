package com.suwiki.remote.timetable.di

import com.suwiki.data.timetable.datasource.RemoteOpenLectureDataSource
import com.suwiki.remote.common.di.firebaseDatabaseModule
import com.suwiki.remote.timetable.datasource.RemoteOpenLectureDataSourceImpl
import org.koin.dsl.module

val remoteTimetableDataSourceModule = module {
  single<RemoteOpenLectureDataSource> { RemoteOpenLectureDataSourceImpl(get()) }
}
