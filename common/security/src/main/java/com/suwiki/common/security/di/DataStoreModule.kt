package com.suwiki.common.security.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.suwiki.common.security.SecurityPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

  @Singleton
  @Provides
  @SecureDataStore
  fun provideEncryptedDataStore(
    @ApplicationContext applicationContext: Context,
  ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
    corruptionHandler = ReplaceFileCorruptionHandler(
      produceNewData = { emptyPreferences() },
    ),
    produceFile = { applicationContext.preferencesDataStoreFile("security-preference") },
  )

  @Singleton
  @Provides
  fun provideSecurityPreference(
    @SecureDataStore dataStore: DataStore<Preferences>,
  ): SecurityPreferences = object : SecurityPreferences {
    override fun flowAccessToken(): Flow<String> = kotlinx.coroutines.flow.flowOf("fake_access_token")

    override suspend fun setAccessToken(value: String) {
      // 아무 동작도 하지 않음 (fake 객체)
    }

    override fun flowRefreshToken(): Flow<String> = kotlinx.coroutines.flow.flowOf("fake_refresh_token")

    override suspend fun setRefreshToken(value: String) {
      // 아무 동작도 하지 않음 (fake 객체)
    }

    override suspend fun clearAll() {
      // 아무 동작도 하지 않음 (fake 객체)
    }
  }
}
