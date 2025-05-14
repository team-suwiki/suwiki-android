package com.suwiki.domain.openmajor.repository

import kotlinx.coroutines.flow.Flow

interface OpenMajorRepository {
  suspend fun getOpenMajorList(): Flow<List<String>>
}
