package com.suwiki.remote.common.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.suwiki.remote.common.BuildConfig
import com.suwiki.remote.common.authenticator.TokenAuthenticator
import com.suwiki.remote.common.interceptor.AuthenticationInterceptor
import com.suwiki.remote.common.retrofit.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton
import org.koin.dsl.module

val networkModule = module {
    single {
        Json {
            prettyPrint = true
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }

    single {
        val json = get<Json>()
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            when {
                !message.isJsonObject() && !message.isJsonArray() ->
                    Timber.tag("RETROFIT_TAG").d("CONNECTION INFO -> $message")

                else -> kotlin.runCatching {
                    json.encodeToString(Json.parseToJsonElement(message))
                }.onSuccess {
                    Timber.tag("RETROFIT_TAG").d(it)
                }.onFailure {
                    Timber.tag("RETROFIT_TAG").d(message)
                }
            }
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        loggingInterceptor
    }

    single(qualifier = org.koin.core.qualifier.named("noAuthOkHttpClient")) {
        OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single(qualifier = org.koin.core.qualifier.named("noAuthRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get(qualifier = org.koin.core.qualifier.named("noAuthOkHttpClient")))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaTypeOrNull()!!))
            .build()
    }

    single(qualifier = org.koin.core.qualifier.named("authOkHttpClient")) {
        OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(get<AuthenticationInterceptor>())
            .authenticator(get<TokenAuthenticator>())
            .build()
    }

    single(qualifier = org.koin.core.qualifier.named("authRetrofit")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get(qualifier = org.koin.core.qualifier.named("authOkHttpClient")))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaTypeOrNull()!!))
            .build()
    }
}

private fun String?.isJsonObject(): Boolean = this?.startsWith("{") == true && this.endsWith("}")
private fun String?.isJsonArray(): Boolean = this?.startsWith("[") == true && this.endsWith("]")
