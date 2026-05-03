package com.example.ben.data.respository

import com.example.ben.data.room.User
import com.example.ben.data.room.UserDataBase

class UserRepository {
    private val userDao = UserDataBase.getDatabase().userDao()
    suspend fun getUserByAccount(account: String): User?{
        return userDao.getUserByAccountUser(account)
    }
    suspend fun updateUser(user: User){
        userDao.updateUserInfo(user)
    }
    suspend fun insertUser(user: User){
        userDao.insertUser(user)
    }
}