package com.example.noteapp.feature_note.data.data_source

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_1_2 = object : Migration(1, 2) {

        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL(
                "ALTER TABLE note ADD COLUMN userId TEXT NOT NULL DEFAULT ''"
            )

            db.execSQL(
                "ALTER TABLE note ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0"
            )

            db.execSQL(
                "ALTER TABLE note ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0"
            )
        }
    }

}