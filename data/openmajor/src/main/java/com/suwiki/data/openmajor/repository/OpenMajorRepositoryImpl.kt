package com.suwiki.data.openmajor.repository

import com.suwiki.core.model.openmajor.OpenMajor
import com.suwiki.data.openmajor.datasource.LocalOpenMajorDataSource
import com.suwiki.data.openmajor.datasource.RemoteOpenMajorDataSource
import com.suwiki.domain.openmajor.repository.OpenMajorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OpenMajorRepositoryImpl @Inject constructor(
  private val localOpenMajorDataSource: LocalOpenMajorDataSource,
  private val remoteOpenMajorDataSource: RemoteOpenMajorDataSource,
) : OpenMajorRepository {
  override suspend fun getOpenMajorList(): Flow<List<String>> = flow {
    emit(localOpenMajorDataSource.getLocalOpenMajorList().map { it.name })

    val localVersion = localOpenMajorDataSource.getLocalOpenMajorVersion().firstOrNull() ?: 0f
    val remoteVersion = remoteOpenMajorDataSource.getOpenMajorVersion()

    if (remoteVersion > localVersion) {
      val remoteOpenMajorList = remoteOpenMajorDataSource.getOpenMajorList()

      emit(remoteOpenMajorList)

      with(localOpenMajorDataSource) {
        deleteAllOpenMajors()
        saveAllOpenMajors(
          remoteOpenMajorList.mapIndexed { index, major ->
            OpenMajor(id = index, name = major)
          },
        )
        setLocalOpenMajorVersion(remoteVersion)
      }
    }
  }.flowOn(Dispatchers.IO)

  override suspend fun getBookmarkedOpenMajorList(): List<String> {
    return remoteOpenMajorDataSource.getBookmarkedMajorList()
  }

  override suspend fun bookmarkMajor(majorName: String) {
    remoteOpenMajorDataSource.bookmarkMajor(majorName)
  }

  override suspend fun removeBookmarkMajor(majorName: String) {
    remoteOpenMajorDataSource.removeBookmarkMajor(majorName)
  }
}
