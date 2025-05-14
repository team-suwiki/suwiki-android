package com.suwiki.remote.openmajor.datasource

import com.suwiki.data.openmajor.datasource.RemoteOpenMajorDataSource
import com.suwiki.remote.openmajor.api.MajorApi

class RemoteOpenMajorDataSourceImpl(private val majorApi: MajorApi) : RemoteOpenMajorDataSource {
  override suspend fun getOpenMajorVersion(): Float {
    return majorApi.getOpenMajorVersion().getOrThrow().version
  }

  override suspend fun getOpenMajorList(): List<String> {
    return majorApi.getOpenMajorList().getOrThrow().data
  }
}
