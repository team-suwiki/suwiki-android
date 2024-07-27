package com.suwiki.data.notice.repository

import com.suwiki.common.model.notice.Notice
import com.suwiki.common.model.notice.NoticeDetail
import com.suwiki.data.notice.datasource.RemoteNoticeDataSource
import com.suwiki.domain.notice.repository.NoticeRepository
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
  private val remoteNoticeDataSource: RemoteNoticeDataSource,
) : NoticeRepository {
  override suspend fun getNoticeList(page: Int): List<Notice> {
    return remoteNoticeDataSource.getNoticeList(page)
  }

  override suspend fun getNoticeDetail(id: Long): NoticeDetail {
    return remoteNoticeDataSource.getNoticeDetail(id)
  }

  override suspend fun checkUpdateMandatory(versionCode: Long): Boolean {
    return remoteNoticeDataSource.checkUpdateMandatory(versionCode)
  }
}
