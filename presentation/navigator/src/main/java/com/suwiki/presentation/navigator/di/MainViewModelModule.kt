package com.suwiki.presentation.navigator.di

import com.suwiki.presentation.navigator.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainViewModelModule = module {
  viewModelOf(::MainViewModel)
}
