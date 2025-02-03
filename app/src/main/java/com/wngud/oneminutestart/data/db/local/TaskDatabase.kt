package com.wngud.oneminutestart.data.db.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [TaskEntity::class], version = 4, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    // DAO 정의
    abstract fun taskDao(): TaskDao

    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }
}