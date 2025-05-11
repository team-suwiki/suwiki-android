package com.suwiki.presentation.openmajor.di

import com.suwiki.presentation.openmajor.OpenMajorViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val openMajorModule = module {
  viewModelOf(::OpenMajorViewModel)
}
