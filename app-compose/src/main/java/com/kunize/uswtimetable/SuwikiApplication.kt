package com.kunize.uswtimetable

import android.app.Application
import com.suwiki.data.openmajor.di.openMajorRepositoryModule
import com.suwiki.data.timetable.di.timetableRepositoryModule
import com.suwiki.domain.timetable.timetableDomainModule
import com.suwiki.local.timetable.di.localTimetableDataSourceModule
import com.suwiki.presentation.openmajor.di.openMajorModule
import com.suwiki.presentation.timetable.di.timetableModule
import com.suwiki.remote.timetable.di.remoteTimetableDataSourceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class SuwikiApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    startKoin {
      androidContext(this@SuwikiApplication)
      modules(
        remoteTimetableDataSourceModule,
        localTimetableDataSourceModule,
        timetableRepositoryModule,
        openMajorRepositoryModule,
        timetableModule,
        openMajorModule,
        timetableDomainModule,
      )
    }
  }
}
