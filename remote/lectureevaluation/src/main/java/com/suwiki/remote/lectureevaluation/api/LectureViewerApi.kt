package com.suwiki.remote.lectureevaluation.api

import com.suwiki.remote.common.retrofit.ApiResult
import com.suwiki.remote.lectureevaluation.response.DataResponse
import com.suwiki.remote.lectureevaluation.response.LectureEvaluationAverageResponse
import com.suwiki.remote.lectureevaluation.response.LectureEvaluationExtraAverageResponse
import com.suwiki.remote.lectureevaluation.response.LectureEvaluationListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LectureViewerApi {
  companion object {
    const val LECTURE: String = "/lecture"

    const val QUERY_SEARCH_VALUE = "searchValue"
    const val QUERY_MAJOR_TYPE = "majorType"
    const val QUERY_PAGE = "page"
    const val QUERY_OPTION = "option"
    const val QUERY_LECTURE_ID = "lectureId"

    const val EVALUATE_POST = "/evaluate-posts"
  }

  // 메인 페이지
  @GET("$LECTURE/all/")
  suspend fun getLectureEvaluationAverageList(
    @Query(QUERY_OPTION) option: String,
    @Query(QUERY_PAGE) page: Int = 1,
    @Query(QUERY_MAJOR_TYPE) majorType: String = "",
  ): ApiResult<DataResponse<List<LectureEvaluationAverageResponse?>>>

  // 통합 검색 결과
  @GET("$LECTURE/search/")
  suspend fun retrieveLectureEvaluationAverageList(
    @Query(QUERY_SEARCH_VALUE) searchValue: String,
    @Query(QUERY_OPTION) option: String,
    @Query(QUERY_PAGE) page: Int,
    @Query(QUERY_MAJOR_TYPE) majorType: String,
  ): ApiResult<DataResponse<List<LectureEvaluationAverageResponse?>>>

  // 검색결과 자세히 보기 (LECTURE)
  @GET("$LECTURE/")
  suspend fun getLectureEvaluationExtraAverage(
    @Query(QUERY_LECTURE_ID) lectureId: Long,
  ): ApiResult<DataResponse<LectureEvaluationExtraAverageResponse>>

  // 검색 결과 자세히 보기 (Evaluation)
  @GET(EVALUATE_POST)
  suspend fun getLectureEvaluationList(
    @Query(QUERY_LECTURE_ID) lectureId: Long,
    @Query(QUERY_PAGE) page: Int,
  ): ApiResult<LectureEvaluationListResponse>
}
