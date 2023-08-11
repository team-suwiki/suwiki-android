package com.suwiki.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.suwiki.local.db.dao.OpenMajorDao
import com.suwiki.local.model.OpenMajorEntity

@Database(
    entities = [OpenMajorEntity::class],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = OpenMajorDatabase.RenameTableAutoMigration::class),
    ],
    exportSchema = true,
)
abstract class OpenMajorDatabase : RoomDatabase() {
    abstract fun openMajorDao(): OpenMajorDao

    @RenameTable(fromTableName = "OpenMajorData", toTableName = "OpenMajorEntity")
    class RenameTableAutoMigration : AutoMigrationSpec
}
