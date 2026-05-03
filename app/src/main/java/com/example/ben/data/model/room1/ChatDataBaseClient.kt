package com.example.ben.data.model.room1

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration

object ChatDataBaseClient {
    @Volatile
    private var INSTANCE: ChatDataBase? = null
    val MIGRATION_1_2 = Migration(1, 2) { database ->
        database.execSQL("ALTER TABLE ChatData ADD COLUMN createTime INTEGER NOT NULL DEFAULT 0")
    }
    fun getDatabase(context: Context): ChatDataBase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ChatDataBase::class.java,
                "chat_database"
            )
            .addMigrations(MIGRATION_1_2)
            .build()
            INSTANCE = instance
            instance
        }
    }
}
