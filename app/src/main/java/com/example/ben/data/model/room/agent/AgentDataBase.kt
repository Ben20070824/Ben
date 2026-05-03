package com.example.ben.data.model.room.agent

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ben.MyApplication
import com.example.ben.tool.MessageConverter

@Database(entities = [AgentData::class], version = 1)
@TypeConverters(MessageConverter::class)
abstract class AgentDataBase : RoomDatabase() {
    abstract fun agentDataDao() : AgentDataDao
    companion object{
        private var INSTANCE : AgentDataBase? = null
        @Synchronized
        fun getDataBase() : AgentDataBase{
            INSTANCE?.let {
                return it
            }
            val instance = Room.databaseBuilder(
                MyApplication.globalContext,
                AgentDataBase::class.java,
                "agent_data"
            ).build()
            INSTANCE= instance
            return instance
        }
    }
}