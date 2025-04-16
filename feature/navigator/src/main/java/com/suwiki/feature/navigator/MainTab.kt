package com.suwiki.feature.navigator

import com.suwiki.feature.lectureevaluation.viewerreporter.navigation.LectureEvaluationRoute
import com.suwiki.feature.myinfo.navigation.MyInfoRoute
import com.suwiki.feature.timetable.navigation.TimetableRoute

internal enum class MainTab(
  val iconResId: Int,
  internal val contentDescription: String,
  val route: String,
) {
  TIMETABLE(
    iconResId = R.drawable.ic_bottom_navigation_timetable,
    contentDescription = "시간표",
    route = TimetableRoute.route,
  ),
  ;

  companion object {
    operator fun contains(route: String): Boolean {
      return entries.map { it.route }.contains(route)
    }

    fun find(route: String): MainTab? {
      return entries.find { it.route == route }
    }
  }
}
