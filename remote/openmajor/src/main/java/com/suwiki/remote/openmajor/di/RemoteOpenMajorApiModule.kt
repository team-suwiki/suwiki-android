package com.suwiki.remote.openmajor.di

import com.suwiki.remote.openmajor.api.MajorApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val remoteOpenMajorApiModule = module {
    single<MajorApi> {
        get<Retrofit>(named("authRetrofit")).create(MajorApi::class.java)
    }
}
