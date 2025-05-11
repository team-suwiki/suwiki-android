package com.suwiki.data.openmajor.di

import com.suwiki.data.openmajor.repository.OpenMajorRepositoryImpl
import com.suwiki.domain.openmajor.repository.OpenMajorRepository
import org.koin.dsl.module

val openMajorRepositoryModule = module {
    single<OpenMajorRepository> { OpenMajorRepositoryImpl(get(), get()) }
}

