package com.suwiki.data.timetable

data class OpenLectureRaw(
  val number: Long = 0, // 번호
  val major: String = "", // 개설학과
  val grade: Int = 1, // 개설학년
  val className: String = "", // 과목명
  val classification: String = "", // 이수 구분
  val professor: String = "",
  val time: String = "",
)
