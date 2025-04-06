package com.suwiki.core.database.database.converter

import androidx.room.TypeConverter
import com.suwiki.core.database.model.CellEntity
import com.suwiki.core.model.timetable.TimetableDay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OpenLectureConverter {
  private val json = Json { ignoreUnknownKeys = true }

  @TypeConverter
  fun fromCellList(value: List<CellEntity>): String {
    return json.encodeToString(value)
  }

  @TypeConverter
  fun toCellList(value: String): List<CellEntity> {
    return json.decodeFromString(value)
  }
}
