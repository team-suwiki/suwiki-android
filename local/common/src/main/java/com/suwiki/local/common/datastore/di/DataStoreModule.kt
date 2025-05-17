package com.suwiki.local.common.datastore.di

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.suwiki.local.common.datastore.proto.UserPreferenceSerializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataStoreModule = module {
  single {
    DataStoreFactory.create(
      serializer = get<UserPreferenceSerializer>(),
    ) {
      androidContext().dataStoreFile("user_preference.pb")
    }
  }

  single(qualifier = named("normalDataStore")) {
    PreferenceDataStoreFactory.create(
      corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() },
      ),
      produceFile = { androidContext().preferencesDataStoreFile("suwiki-preference") },
    )
  }
}
