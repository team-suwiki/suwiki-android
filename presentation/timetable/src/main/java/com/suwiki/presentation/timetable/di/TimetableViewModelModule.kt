package com.suwiki.presentation.timetable.di

import org.koin.core.module.dsl.viewModel
import androidx.lifecycle.SavedStateHandle
import com.suwiki.presentation.timetable.celleditor.CellEditorViewModel
import com.suwiki.presentation.timetable.openlecture.OpenLectureViewModel
import com.suwiki.presentation.timetable.timetable.TimetableViewModel
import com.suwiki.presentation.timetable.timetableeditor.TimetableEditorViewModel
import com.suwiki.presentation.timetable.timetablelist.TimetableListViewModel
import org.koin.dsl.module

val timetableViewModelModule = module {
  viewModel { (savedStateHandle: SavedStateHandle) ->
    CellEditorViewModel(get(), get(), savedStateHandle)
  }
  viewModel { OpenLectureViewModel(get(), get(), get(), get()) }
  viewModel { TimetableViewModel(get(), get(), get(), get()) }
  viewModel { (savedStateHandle: SavedStateHandle) ->
    TimetableEditorViewModel(get(), get(), savedStateHandle)
  }
  viewModel { TimetableListViewModel(get(), get(), get()) }
}
