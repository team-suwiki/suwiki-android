package com.suwiki.domain.timetable

import com.suwiki.domain.timetable.usecase.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val timetableDomainModule = module {
    // Timetable management use cases
    factoryOf(::DeleteTimetableUseCase)
    factoryOf(::GetAllTimetableUseCase)
    factoryOf(::GetMainTimetableUseCase)
    factoryOf(::InsertTimetableUseCase)
    factoryOf(::UpdateTimetableUseCase)
    factoryOf(::SetMainTimetableCreateTime)

    // Timetable cell use cases
    factoryOf(::DeleteTimetableCellUseCase)
    factoryOf(::InsertTimetableCellUseCase)
    factoryOf(::UpdateTimetableCellUseCase)
    factoryOf(::GetTimetableCellTypeUseCase)
    factoryOf(::SetTimetableCellTypeUseCase)

    // Open lecture use cases
    factoryOf(::GetOpenLectureListUseCase)
    factoryOf(::UpdateOpenLectureIfNeedUseCase)
}
