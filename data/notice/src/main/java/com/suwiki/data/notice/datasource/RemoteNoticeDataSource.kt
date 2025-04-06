package com.suwiki.data.notice.datasource

import com.suwiki.core.model.notice.Notice
import com.suwiki.core.model.notice.NoticeDetail

interface RemoteNoticeDataSource {

  suspend fun getNoticeList(page: Int): List<Notice>

  suspend fun getNoticeDetail(id: Long): NoticeDetail

  suspend fun getMinVersionCode(): Long?
}
