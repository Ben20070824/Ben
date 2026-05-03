package com.example.ben.data.model.room.chat

import android.content.Context
import androidx.room.Room

object ChatDataBaseClient {
    private var INSTANCE: ChatDataBase? = null
    @Synchronized
    fun getDatabase(context: Context): ChatDataBase {
        INSTANCE?.let {
            return it
        }
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ChatDataBase::class.java,
                "chat_database"
            )
            .build()
            INSTANCE = instance
        return instance
    }
}
