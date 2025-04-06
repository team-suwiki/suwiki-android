package com.suwiki.remote.notice.datasource

import com.google.firebase.database.FirebaseDatabase
import com.suwiki.core.model.notice.Notice
import com.suwiki.core.model.notice.NoticeDetail
import com.suwiki.data.notice.datasource.RemoteNoticeDataSource
import com.suwiki.remote.notice.api.NoticeApi
import com.suwiki.remote.notice.response.toModel
import kotlinx.coroutines.tasks.await
import okhttp3.internal.toLongOrDefault
import javax.inject.Inject

class RemoteNoticeDataSourceImpl @Inject constructor(
  private val noticeApi: NoticeApi,
  private val firebaseDatabase: FirebaseDatabase,
) : RemoteNoticeDataSource {

  override suspend fun getNoticeList(page: Int): List<Notice> {
    return noticeApi.getNoticeList(page).getOrThrow().data.map { it.toModel() }
  }

  override suspend fun getNoticeDetail(id: Long): NoticeDetail {
    return noticeApi.getNotice(id).getOrThrow().data.toModel()
  }

  override suspend fun getMinVersionCode(): Long? {
    return firebaseDatabase
      .getReference(DATABASE_MIN_VERSION_CODE)
      .get()
      .await()
      .value
      .toString()
      .toLongOrNull()
  }

  companion object {
    private const val DATABASE_MIN_VERSION_CODE = "minVersionCode"
  }
}
