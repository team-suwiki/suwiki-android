package com.suwiki.local.common

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.suwiki.local.common.database.database.TimetableDatabase
import com.suwiki.local.common.database.di.DatabaseName
import com.suwiki.local.common.database.migration.TIMETABLE_MIGRATION_1_2
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TimetableDatabaseMigrate1To2Test {

  @get:Rule
  val helper: MigrationTestHelper = MigrationTestHelper(
    InstrumentationRegistry.getInstrumentation(),
    TimetableDatabase::class.java,
  )

  @Test
  @Throws(IOException::class)
  fun testTimetableDBMigrate1To2() {
    helper.createDatabase(DatabaseName.TIMETABLE, 1).apply {
      val testCellList = """
        [
          {"color":-9728172,"credit":"","day":"목","endTime":"4","location":"미래520","name":"도전과창조-기업가정신","professor":"김기선","startTime":"3"},
          {"color":-4026526,"credit":"","day":"화","endTime":"6","location":"미래520","name":"도전과창조-기업가정신","professor":"홍태민","startTime":"5"},
          {"color":-96120,"credit":"","day":"화","endTime":"1","location":"인문407","name":"언어와문화(이러닝)","professor":"김동섭","startTime":"1"},
          {"color":-96120,"credit":"","day":"토","endTime":"4","location":"인문407","name":"언어와문화(이러닝)","professor":"김동섭","startTime":"3"},
          {"color":-12363882,"credit":"","day":"","endTime":"","location":"이러닝","name":"도전과창조-기업가정신","professor":"김기선","startTime":""},
          {"color":-6194752,"credit":"","day":"토","endTime":"6","location":"미래520","name":"도전과창조-기업가정신","professor":"김기선","startTime":"5"}
        ]
      """.trimIndent()

      execSQL(
        """
          INSERT INTO TimetableList (createTime, year, semester, timeTableName, timeTableJsonData)
          VALUES ('1706152141568', '2024', '1', '테스트 시간표 이름', '$testCellList')
        """.trimIndent(),
      )

      close()
    }

    helper.runMigrationsAndValidate(
      /* name = */ DatabaseName.TIMETABLE,
      /* version = */ 1,
      /* validateDroppedTables = */ true,
      /* ...migrations = */ TIMETABLE_MIGRATION_1_2,
    )
  }
}
