package com.suwiki.remote.openmajor.di

import com.suwiki.data.openmajor.datasource.RemoteOpenMajorDataSource
import com.suwiki.remote.openmajor.datasource.RemoteOpenMajorDataSourceImpl
import org.koin.dsl.module

val remoteOpenMajorDataSourceModule = module {
  single<RemoteOpenMajorDataSource> { RemoteOpenMajorDataSourceImpl(get()) }
}
