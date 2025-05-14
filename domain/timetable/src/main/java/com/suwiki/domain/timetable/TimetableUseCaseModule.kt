package com.suwiki.domain.timetable

import com.suwiki.domain.timetable.usecase.*
import org.koin.dsl.module

val timetableUseCaseModule = module {
    // Timetable management use cases
    factory { DeleteTimetableUseCase(get()) }
    factory { GetAllTimetableUseCase(get()) }
    factory { GetMainTimetableUseCase(get()) }
    factory { InsertTimetableUseCase(get()) }
    factory { UpdateTimetableUseCase(get()) }
    factory { SetMainTimetableCreateTime(get()) }

    // Timetable cell use cases
    factory { DeleteTimetableCellUseCase(get()) }
    factory { InsertTimetableCellUseCase(get()) }
    factory { UpdateTimetableCellUseCase(get()) }
    factory { GetTimetableCellTypeUseCase(get()) }
    factory { SetTimetableCellTypeUseCase(get()) }

    // Open lecture use cases
    factory { GetOpenLectureListUseCase(get()) }
    factory { UpdateOpenLectureIfNeedUseCase(get()) }
}
