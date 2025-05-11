package com.suwiki.data.timetable.di

import com.suwiki.data.timetable.repository.OpenLectureRepositoryImpl
import com.suwiki.data.timetable.repository.TimetableRepositoryImpl
import com.suwiki.domain.timetable.repository.OpenLectureRepository
import com.suwiki.domain.timetable.repository.TimetableRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val timetableRepositoryModule = module {
  singleOf(::OpenLectureRepositoryImpl) { bind<OpenLectureRepository>() }
  singleOf(::TimetableRepositoryImpl) { bind<TimetableRepository>() }
}
