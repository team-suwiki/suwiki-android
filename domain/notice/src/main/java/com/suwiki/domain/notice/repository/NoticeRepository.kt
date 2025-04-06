package com.suwiki.domain.notice.repository

import com.suwiki.core.model.notice.Notice
import com.suwiki.core.model.notice.NoticeDetail

typealias NeedMandatoryUpdate = Boolean

interface NoticeRepository {

  suspend fun getNoticeList(page: Int): List<Notice>

  suspend fun getNoticeDetail(id: Long): NoticeDetail

  suspend fun checkUpdateMandatory(
    versionCode: Long,
  ): NeedMandatoryUpdate
}
