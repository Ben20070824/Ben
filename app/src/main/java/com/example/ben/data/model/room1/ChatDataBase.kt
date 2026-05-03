package com.example.ben.data.model.room1

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ben.tool.MessageConverter

@Database(entities = [ChatData::class], version = 1, exportSchema = false)
@TypeConverters(MessageConverter::class)
abstract class ChatDataBase : RoomDatabase() {
    abstract fun chatDataDao(): ChatDataDao
}
