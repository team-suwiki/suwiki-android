package com.suwiki.presentation.timetable.di

import com.suwiki.presentation.timetable.openlecture.OpenLectureViewModel
import com.suwiki.presentation.timetable.timetable.TimetableViewModel
import com.suwiki.presentation.timetable.timetableeditor.TimetableEditorViewModel
import com.suwiki.presentation.timetable.timetablelist.TimetableListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val timetableModule = module {
  viewModelOf(::OpenLectureViewModel)
  viewModelOf(::TimetableViewModel)
  viewModelOf(::TimetableEditorViewModel)
  viewModelOf(::TimetableListViewModel)
}
