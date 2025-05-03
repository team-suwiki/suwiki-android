package com.suwiki.common.security

import kotlinx.coroutines.flow.Flow

interface SecurityPreferences {

  fun flowAccessToken(): Flow<String>

  suspend fun setAccessToken(value: String)

  fun flowRefreshToken(): Flow<String>

  suspend fun setRefreshToken(value: String)

  suspend fun clearAll()

  companion object {

    private const val KEY_ACCESS_TOKEN = "key-access-token"
    private const val KEY_REFRESH_TOKEN = "key-refresh-token"
  }
}
