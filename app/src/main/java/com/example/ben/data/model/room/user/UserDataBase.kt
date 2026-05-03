package com.example.ben.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ben.MyApplication

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: UserDataBase? = null

        @Synchronized
        fun getDatabase(): UserDataBase {
            INSTANCE?.let {
                return it
            }

            val instance = Room.databaseBuilder(
                MyApplication.globalContext,
                UserDataBase::class.java,
                "user_management_db"
            )
            .build()

            INSTANCE = instance
            return instance
        }
    }
}
