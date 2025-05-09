package com.suwiki.local.openmajor.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import com.suwiki.common.android.Dispatcher
import com.suwiki.common.android.SuwikiDispatchers
import com.suwiki.common.model.openmajor.OpenMajor
import com.suwiki.data.openmajor.datasource.LocalOpenMajorDataSource
import com.suwiki.local.common.database.database.OpenMajorDatabase
import com.suwiki.local.common.datastore.di.NormalDataStore
import com.suwiki.local.openmajor.converter.toEntity
import com.suwiki.local.openmajor.converter.toModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalOpenMajorDataSourceImpl @Inject constructor(
  @NormalDataStore private val dataStore: DataStore<Preferences>,
  @Dispatcher(SuwikiDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
  private val db: OpenMajorDatabase,
) : LocalOpenMajorDataSource {

  companion object {
    private val OPEN_MAJOR_VERSION = floatPreferencesKey("[KEY] is open major version")
  }

  private val data: Flow<Preferences>
    get() = dataStore.data

  override suspend fun getLocalOpenMajorVersion(): Flow<Float> =
    data.map { it[OPEN_MAJOR_VERSION] ?: 0f }

  override suspend fun setLocalOpenMajorVersion(version: Float) {
    dataStore.edit { it[OPEN_MAJOR_VERSION] = version }
  }

  override suspend fun getLocalOpenMajorList(): List<OpenMajor> = withContext(ioDispatcher) {
    db.openMajorDao().getAll().map { it.toModel() }
  }

  override suspend fun saveAllOpenMajors(majors: List<OpenMajor>) = withContext(ioDispatcher) {
    db.openMajorDao().insertAll(majors.map { it.toEntity() })
  }

  override suspend fun deleteAllOpenMajors() = withContext(ioDispatcher) {
    db.openMajorDao().deleteAll()
  }
}
