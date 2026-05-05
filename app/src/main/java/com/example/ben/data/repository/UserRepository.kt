package com.example.ben.data.repository

import android.content.Context
import com.example.ben.MyApplication
import com.example.ben.data.model.room.user.User
import com.example.ben.data.model.room.user.UserDataBase

class UserRepository {
    private val userDao = UserDataBase.getDatabase().userDao()
    val sp = MyApplication.globalContext.getSharedPreferences("auto_login", Context.MODE_PRIVATE)
    suspend fun getUserByAccount(account: String): User?{
        return userDao.getUserByAccountUser(account)
    }
    suspend fun updateUser(user: User){
        userDao.updateUserInfo(user)
    }
    suspend fun insertUser(user: User){
        userDao.insertUser(user)
    }

    fun login(account: String){
        val edit = sp.edit()
        edit.putBoolean("auto_login",true)
        edit.putString("account",account)
        edit.apply()
    }
    fun unLogin(){
        val edit = sp.edit()
        edit.remove("auto_login")
        edit.remove("account")
        edit.apply()
    }
    fun autoLogin() : String{
        val autoLogin = sp.getBoolean("auto_login", false)
        if(!autoLogin)    return ""
        val string = sp.getString("account", "")
        return string ?: ""
    }
}