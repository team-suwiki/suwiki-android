package com.suwiki.local.timetable.converter

import com.suwiki.core.database.model.CellEntity
import com.suwiki.core.database.model.OpenLectureEntity
import com.suwiki.core.database.model.TimetableEntity
import com.suwiki.core.model.timetable.Cell
import com.suwiki.core.model.timetable.OpenLecture
import com.suwiki.core.model.timetable.Timetable

fun OpenLectureEntity.toModel() = OpenLecture(
  id = id,
  name = name,
  type = type,
  major = major,
  grade = grade,
  professorName = professorName,
  originalCellList = cellList.map { it.toModel() }
)

fun OpenLecture.toEntity() = OpenLectureEntity(
  id = id,
  name = name,
  type = type,
  major = major,
  grade = grade,
  professorName = professorName,
  cellList = originalCellList.map { it.toEntity() }
)

fun Cell.toEntity() = CellEntity(
  location = location,
  day = day,
  startPeriod = startPeriod,
  endPeriod = endPeriod,
)

fun CellEntity.toModel() = Cell(
  location = location,
  day = day,
  startPeriod = startPeriod,
  endPeriod = endPeriod,
)
