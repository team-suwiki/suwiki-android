package com.suwiki.domain.openmajor.usecase

import com.suwiki.domain.openmajor.repository.OpenMajorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetOpenMajorListUseCase (private val openMajorRepository: OpenMajorRepository, ) {
  /**
   * 가장 먼저 LocalOpenMajorList를 emit합니다.
   * 그 이후 LocalVersion과 RemoteVersion을 비교한 후
   * 만약 RemoteVersion이 더 크다면 최신 데이터가 존재하는 것이므로
   * RemoteOpenMajorList를 받아온 후 emit합니다.
   *
   * 그 이후 LocalVersion과 LocalOpenMajorList를 최신화 합니다.
   */
  suspend operator fun invoke(): Flow<List<String>> {
    return openMajorRepository.getOpenMajorList().map { listOf("전체") + it } // TODO v2 api 리팩토링
  }
}
