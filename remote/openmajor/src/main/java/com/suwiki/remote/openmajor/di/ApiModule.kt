package com.suwiki.remote.openmajor.di

import com.suwiki.remote.common.di.AuthRetrofit
import com.suwiki.remote.openmajor.api.MajorApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import javax.inject.Singleton

val openMajorModule = module {
    single<MajorApi> {
        get<Retrofit>(named("authRetrofit")).create(MajorApi::class.java)
    }
}
