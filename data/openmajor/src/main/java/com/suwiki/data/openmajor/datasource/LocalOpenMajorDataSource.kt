package com.suwiki.data.openmajor.datasource

import com.suwiki.core.model.openmajor.OpenMajor
import kotlinx.coroutines.flow.Flow

interface LocalOpenMajorDataSource {
  suspend fun getLocalOpenMajorVersion(): Flow<Float>
  suspend fun setLocalOpenMajorVersion(version: Float)
  suspend fun getLocalOpenMajorList(): List<OpenMajor>
  suspend fun saveAllOpenMajors(majors: List<OpenMajor>)
  suspend fun deleteAllOpenMajors()

  fun isSuwikiServerEndPopupVisible(): Flow<Boolean>
  suspend fun setShowSuwikiServerEndPopup(visible: Boolean)
}
