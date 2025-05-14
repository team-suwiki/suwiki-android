package com.kunize.uswtimetable

import android.app.Application
import com.suwiki.data.openmajor.di.openMajorRepositoryModule
import com.suwiki.data.timetable.di.timetableRepositoryModule
import com.suwiki.domain.openmajor.di.openMajorUseCaseModule
import com.suwiki.domain.timetable.timetableUseCaseModule
import com.suwiki.local.common.database.di.daoModule
import com.suwiki.local.common.datastore.di.dataStoreModule
import com.suwiki.local.common.database.di.databaseModule
import com.suwiki.local.openmajor.di.localOpenMajorDataSourceModule
import com.suwiki.local.timetable.di.localTimetableDataSourceModule
import com.suwiki.presentation.navigator.di.mainViewModelModule
import com.suwiki.presentation.openmajor.di.openMajorViewModelModule
import com.suwiki.presentation.timetable.di.timetableViewModelModule
import com.suwiki.remote.common.di.apiModule
import com.suwiki.remote.common.di.firebaseDatabaseModule
import com.suwiki.remote.common.di.networkModule
import com.suwiki.remote.openmajor.di.remoteOpenMajorApiModule
import com.suwiki.remote.openmajor.di.remoteOpenMajorDataSourceModule
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
        // data module
        openMajorRepositoryModule,
        timetableRepositoryModule,

        // domain module
        openMajorUseCaseModule,
        timetableUseCaseModule,

        // presentation module
        mainViewModelModule,
        openMajorViewModelModule,
        timetableViewModelModule,

        // local module
        localOpenMajorDataSourceModule,
        localTimetableDataSourceModule,
        // common
        daoModule,
        databaseModule,
        dataStoreModule,

        // remote module
        remoteOpenMajorApiModule,
        remoteOpenMajorDataSourceModule,
        remoteTimetableDataSourceModule,
        // common
        apiModule,
        networkModule,
        firebaseDatabaseModule,
      )
    }
  }
}
