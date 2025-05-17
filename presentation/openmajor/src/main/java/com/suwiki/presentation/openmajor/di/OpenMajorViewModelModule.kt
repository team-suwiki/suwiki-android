package com.suwiki.presentation.openmajor.di

import androidx.lifecycle.SavedStateHandle
import com.suwiki.presentation.openmajor.OpenMajorViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val openMajorViewModelModule = module {
  viewModel { (savedStateHandle: SavedStateHandle) ->
    OpenMajorViewModel(get(), get(), savedStateHandle)
  }
}
