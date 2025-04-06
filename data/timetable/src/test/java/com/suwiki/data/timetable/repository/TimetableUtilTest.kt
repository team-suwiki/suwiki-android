package com.suwiki.data.timetable.repository

import TimetableUtil
import com.suwiki.core.model.timetable.Cell
import com.suwiki.core.model.timetable.TimetableDay
import org.junit.Assert.*
import org.junit.Test

class TimetableUtilTest {

  @Test
  fun testParseTimeTableString_MultipleCellsWithMultipleDays() {
    val input = "생활202(화9,10),생활302(수6,7,8 금10)"
    val expected = listOf(
      Cell(location = "생활202", day = TimetableDay.TUE, startPeriod = 9, endPeriod = 10),
      Cell(location = "생활302", day = TimetableDay.WED, startPeriod = 6, endPeriod = 8),
      Cell(location = "생활302", day = TimetableDay.FRI, startPeriod = 10, endPeriod = 10)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_MultipleCellsWithDifferentDays() {
    val input = "생활202(목9,10),생활302(화6,7,8 금9)"
    val expected = listOf(
      Cell(location = "생활202", day = TimetableDay.THU, startPeriod = 9, endPeriod = 10),
      Cell(location = "생활302", day = TimetableDay.TUE, startPeriod = 6, endPeriod = 8),
      Cell(location = "생활302", day = TimetableDay.FRI, startPeriod = 9, endPeriod = 9)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_SingleCellWithNonConsecutivePeriods() {
    val input = "2공학204(화1,2,3,5,6,7)"
    val expected = listOf(
      Cell(location = "2공학204", day = TimetableDay.TUE, startPeriod = 1, endPeriod = 3),
      Cell(location = "2공학204", day = TimetableDay.TUE, startPeriod = 5, endPeriod = 7)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_SingleCellWithNonConsecutivePeriodsOnFriday() {
    val input = "2공학305(금1,2,3,6,7,8)"
    val expected = listOf(
      Cell(location = "2공학305", day = TimetableDay.FRI, startPeriod = 1, endPeriod = 3),
      Cell(location = "2공학305", day = TimetableDay.FRI, startPeriod = 6, endPeriod = 8)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_MultipleCellsOnSameDay() {
    val input = "자연대505(월5,6),자연대506(월7,8)"
    val expected = listOf(
      Cell(location = "자연대505", day = TimetableDay.MON, startPeriod = 5, endPeriod = 6),
      Cell(location = "자연대506", day = TimetableDay.MON, startPeriod = 7, endPeriod = 8)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_MultipleDaysIncludingSaturday() {
    val input = "인문107(월1 토3,4)"
    val expected = listOf(
      Cell(location = "인문107", day = TimetableDay.MON, startPeriod = 1, endPeriod = 1),
      Cell(location = "인문107", day = TimetableDay.SAT, startPeriod = 3, endPeriod = 4)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_SingleCellWithConsecutivePeriods() {
    val input = "미래521(수3,4)"
    val expected = listOf(
      Cell(location = "미래521", day = TimetableDay.WED, startPeriod = 3, endPeriod = 4)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_SingleCell() {
    val input = "생활202(화9,10)"
    val expected = listOf(
      Cell(location = "생활202", day = TimetableDay.TUE, startPeriod = 9, endPeriod = 10)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_MultipleCells() {
    val input = "생활202(화9,10),생활302(수6,7,8 금10)"
    val expected = listOf(
      Cell(location = "생활202", day = TimetableDay.TUE, startPeriod = 9, endPeriod = 10),
      Cell(location = "생활302", day = TimetableDay.WED, startPeriod = 6, endPeriod = 8),
      Cell(location = "생활302", day = TimetableDay.FRI, startPeriod = 10, endPeriod = 10)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_ConsecutivePeriods() {
    val input = "2공학204(화1,2,3,5,6,7)"
    val expected = listOf(
      Cell(location = "2공학204", day = TimetableDay.TUE, startPeriod = 1, endPeriod = 3),
      Cell(location = "2공학204", day = TimetableDay.TUE, startPeriod = 5, endPeriod = 7)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_MultipleDaysAndLocations() {
    val input = "자연대505(월5,6),자연대506(월7,8)"
    val expected = listOf(
      Cell(location = "자연대505", day = TimetableDay.MON, startPeriod = 5, endPeriod = 6),
      Cell(location = "자연대506", day = TimetableDay.MON, startPeriod = 7, endPeriod = 8)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_Saturday() {
    val input = "인문107(월1 토3,4)"
    val expected = listOf(
      Cell(location = "인문107", day = TimetableDay.MON, startPeriod = 1, endPeriod = 1),
      Cell(location = "인문107", day = TimetableDay.SAT, startPeriod = 3, endPeriod = 4)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_InvalidInput() {
    val input = "InvalidInput"
    val expected = emptyList<Cell>()
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_EmptyInput() {
    val input = ""
    val expected = emptyList<Cell>()
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }

  @Test
  fun testParseTimeTableString_MultipleLocationsWithComma() {
    val input = "자연대505(월5,6),자연대506(월7,8),공대301(화1,2,3)"
    val expected = listOf(
      Cell(location = "자연대505", day = TimetableDay.MON, startPeriod = 5, endPeriod = 6),
      Cell(location = "자연대506", day = TimetableDay.MON, startPeriod = 7, endPeriod = 8),
      Cell(location = "공대301", day = TimetableDay.TUE, startPeriod = 1, endPeriod = 3)
    )
    assertEquals(expected, TimetableUtil.parseTimeTableString(input))
  }
}
