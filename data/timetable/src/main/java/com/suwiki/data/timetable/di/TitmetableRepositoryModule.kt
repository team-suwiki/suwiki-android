package com.suwiki.data.timetable.di

import com.suwiki.data.timetable.repository.OpenLectureRepositoryImpl
import com.suwiki.data.timetable.repository.TimetableRepositoryImpl
import com.suwiki.domain.timetable.repository.OpenLectureRepository
import com.suwiki.domain.timetable.repository.TimetableRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val timetableRepositoryModule = module {
  single<OpenLectureRepository> { OpenLectureRepositoryImpl(get(), get()) }
  single<TimetableRepository> { TimetableRepositoryImpl(get()) }
}
