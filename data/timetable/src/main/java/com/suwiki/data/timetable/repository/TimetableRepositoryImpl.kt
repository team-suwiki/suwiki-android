package com.suwiki.data.timetable.repository

import com.suwiki.common.model.exception.TimetableCellOverlapException
import com.suwiki.common.model.exception.TimetableCellPeriodInvalidException
import com.suwiki.common.model.timetable.Timetable
import com.suwiki.common.model.timetable.TimetableCell
import com.suwiki.common.model.timetable.TimetableDay
import com.suwiki.data.timetable.datasource.LocalTimetableDataSource
import com.suwiki.domain.timetable.repository.TimetableRepository
import kotlinx.coroutines.flow.firstOrNull

class TimetableRepositoryImpl (
  private val localTimetableDataSource: LocalTimetableDataSource,
) : TimetableRepository {
  override suspend fun getAllTimetable(): List<Timetable> {
    return localTimetableDataSource.getAllTimetable()
  }

  override suspend fun getTimetable(createTime: Long): Timetable? {
    return localTimetableDataSource.getTimetable(createTime)
  }

  override suspend fun setMainTimetableCreateTime(createTime: Long) {
    localTimetableDataSource.setMainTimetableCreateTime(createTime)
  }

  override suspend fun getMainTimetableCreateTime(): Long? {
    return localTimetableDataSource
      .getMainTimetableCreateTime()
      .firstOrNull()
      ?: getAllTimetable()
        .firstOrNull()
        ?.createTime
        ?.apply {
          setMainTimetableCreateTime(this)
        }
  }

  override suspend fun deleteTimetable(data: Timetable) {
    localTimetableDataSource.deleteTimetable(data)
  }

  override suspend fun insertTimetable(data: Timetable) {
    localTimetableDataSource.insertTimetable(data)
  }

  override suspend fun updateTimetable(
    createTime: Long,
    year: String,
    semester: String,
    name: String,
  ) = with(localTimetableDataSource) {
    val timetable = getTimetable(createTime)
    updateTimetable(
      timetable!!.copy(
        year = year,
        semester = semester,
        name = name,
      ),
    )
  }

  override suspend fun insertTimetableCell(cellList: List<TimetableCell>) {
    val timetable = getMainTimetable()!!
    checkPeriodInvalid(cellList)
    checkCellOverlap(cellList, cellList)
    checkCellOverlap(cellList, timetable.cellList)
    localTimetableDataSource.updateTimetable(
      timetable.copy(
        cellList = timetable.cellList.plus(cellList),
      ),
    )
  }

  override suspend fun deleteTimetableCell(cell: TimetableCell): Timetable {
    val timetable = getMainTimetable()!!
      .run {
        copy(cellList = cellList.minus(cell))
      }
    localTimetableDataSource.updateTimetable(
      timetable,
    )
    return timetable
  }

  override suspend fun updateTimetableCell(oldCellId: String, cellList: List<TimetableCell>) {
    val timetable = getMainTimetable()!!
    val tempCellList = timetable.cellList.filter { it.id != oldCellId }

    checkPeriodInvalid(cellList)
    checkCellOverlap(cellList, cellList)
    checkCellOverlap(cellList, tempCellList)

    localTimetableDataSource.updateTimetable(
      timetable.copy(
        cellList = tempCellList.plus(cellList),
      ),
    )
  }

  private suspend fun getMainTimetable() = with(localTimetableDataSource) {
    val createTime = getMainTimetableCreateTime().firstOrNull() ?: return@with null
    getTimetable(createTime)
  }

  private fun checkPeriodInvalid(
    cellList: List<TimetableCell>,
  ) {
    cellList.filter { it.day != TimetableDay.E_LEARNING }.forEach { cell ->
      val isNotValid = cell.startPeriod > cell.endPeriod ||
        cell.startPeriod !in 1..15 ||
        cell.endPeriod !in 1..15
      if (isNotValid) {
        throw TimetableCellPeriodInvalidException(
          startPeriod = cell.startPeriod,
          endPeriod = cell.endPeriod,
        )
      }
    }
  }

  private fun checkCellOverlap(
    toInsertCellList: List<TimetableCell>,
    currentCellList: List<TimetableCell>,
  ) {
    toInsertCellList.forEach { toInsertCell ->
      currentCellList
        .filter { it.day == toInsertCell.day }
        .filter { it.id != toInsertCell.id }
        .filter { it.day != TimetableDay.E_LEARNING }
        .forEach { sameDayCell ->
          val isOverlap = toInsertCell.startPeriod in sameDayCell.startPeriod..sameDayCell.endPeriod ||
            toInsertCell.endPeriod in sameDayCell.startPeriod..sameDayCell.endPeriod ||
            sameDayCell.startPeriod in toInsertCell.startPeriod..toInsertCell.endPeriod ||
            sameDayCell.endPeriod in toInsertCell.startPeriod..toInsertCell.endPeriod

          if (isOverlap) {
            throw TimetableCellOverlapException(
              name = sameDayCell.name,
              startPeriod = sameDayCell.startPeriod,
              endPeriod = sameDayCell.endPeriod,
            )
          }
        }
    }
  }

  override suspend fun getTimetableCellType(): String {
    return localTimetableDataSource.getTimetableCellType().firstOrNull() ?: ""
  }

  override suspend fun setTimetableCellType(type: String) {
    localTimetableDataSource.setTimetableCellType(type)
  }
}
