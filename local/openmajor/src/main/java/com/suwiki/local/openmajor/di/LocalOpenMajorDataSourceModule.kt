package com.suwiki.local.openmajor.di

import com.suwiki.data.openmajor.datasource.LocalOpenMajorDataSource
import com.suwiki.local.openmajor.datasource.LocalOpenMajorDataSourceImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val localOpenMajorDataSourceModule = module {
  single<LocalOpenMajorDataSource> {
    LocalOpenMajorDataSourceImpl(get(named("normalDataStore")), get())
  }
}

