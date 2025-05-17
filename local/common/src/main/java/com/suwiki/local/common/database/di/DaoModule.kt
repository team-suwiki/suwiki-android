package com.suwiki.local.common.database.di

import com.suwiki.local.common.database.dao.OpenLectureDao
import com.suwiki.local.common.database.database.OpenMajorDatabase
import com.suwiki.local.common.database.database.TimetableDatabase
import com.suwiki.local.common.database.dao.OpenMajorDao
import com.suwiki.local.common.database.dao.TimeTableDao
import com.suwiki.local.common.database.database.OpenLectureDatabase
import org.koin.dsl.module

val daoModule = module {
  single<OpenMajorDao> { get<OpenMajorDatabase>().openMajorDao() }
  single<TimeTableDao> { get<TimetableDatabase>().timetableDao() }
  single<OpenLectureDao> { get<OpenLectureDatabase>().openLectureDao() }
}
