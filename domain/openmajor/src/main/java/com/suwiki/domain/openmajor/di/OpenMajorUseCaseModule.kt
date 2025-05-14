package com.suwiki.domain.openmajor.di

import com.suwiki.domain.openmajor.usecase.GetOpenMajorListUseCase
import org.koin.dsl.module

val openMajorUseCaseModule = module {
    factory { GetOpenMajorListUseCase(get()) }
}
