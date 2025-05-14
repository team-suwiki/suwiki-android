package com.suwiki.remote.common.di

import com.suwiki.remote.common.api.AuthApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit


val apiModule = module {
  single<AuthApi> {
    get<Retrofit>(qualifier = named("NoAuthRetrofit")).create(AuthApi::class.java)
  }
}

