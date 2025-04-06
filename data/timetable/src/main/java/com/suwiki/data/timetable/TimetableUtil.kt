import com.suwiki.core.model.timetable.Cell
import com.suwiki.core.model.timetable.TimetableDay

object TimetableUtil {
  fun parseTimeTableString(input: String): List<Cell> {
    val cellRegex = """([^(]+)\(([^)]+)\)""".toRegex()
    val dayPeriodRegex = """([월화수목금토])([\d,]+)""".toRegex()

    val result = input.split("),").flatMap { cellInput ->
      val sanitizedInput = if (!cellInput.endsWith(")")) "$cellInput)" else cellInput
      cellRegex.find(sanitizedInput)?.let { cellMatch ->
        val (location, schedule) = cellMatch.destructured
        val trimmedLocation = location.trim()
        dayPeriodRegex.findAll(schedule).flatMap { dayPeriodMatch ->
          val (day, periods) = dayPeriodMatch.destructured
          val periodList = periods.split(",").map { it.toInt() }

          periodList.groupConsecutive().map { (start, end) ->
            Cell(
              location = trimmedLocation,
              day = when (day) {
                "월" -> TimetableDay.MON
                "화" -> TimetableDay.TUE
                "수" -> TimetableDay.WED
                "목" -> TimetableDay.THU
                "금" -> TimetableDay.FRI
                "토" -> TimetableDay.SAT
                else -> throw IllegalArgumentException("Invalid day: $day")
              },
              startPeriod = start,
              endPeriod = end
            )
          }
        }.toList()
      } ?: emptyList()
    }

    return result
  }

  private fun List<Int>.groupConsecutive(): List<Pair<Int, Int>> {
    if (isEmpty()) return emptyList()
    val result = mutableListOf<Pair<Int, Int>>()
    var start = this[0]
    var prev = start
    for (i in 1 until size) {
      if (this[i] != prev + 1) {
        result.add(start to prev)
        start = this[i]
      }
      prev = this[i]
    }
    result.add(start to prev)
    return result
  }
}
