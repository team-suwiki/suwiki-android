package com.suwiki.remote.lectureevaluation.api

import com.suwiki.remote.common.retrofit.ApiResult
import com.suwiki.remote.lectureevaluation.response.DataResponse
import com.suwiki.remote.lectureevaluation.response.MyLectureEvaluationResponse
import retrofit2.http.GET
import retrofit2.http.Query

// TODO : v2 api로 업그레이드 필요.
interface LectureMyApi {
  companion object {
    const val QUERY_PAGE = "page"
    const val EVALUATE_POST = "/evaluate-posts"
  }

  // 내가 쓴 글 (강의평가)
  @GET("$EVALUATE_POST/written")
  suspend fun getMyLectureEvaluationList(@Query(QUERY_PAGE) page: Int): ApiResult<DataResponse<List<MyLectureEvaluationResponse>>>
}
