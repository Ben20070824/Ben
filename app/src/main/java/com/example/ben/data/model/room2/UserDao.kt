package com.example.ben.data.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(list: List<User>) : LongArray

    @Query("SELECT * FROM users WHERE nickname = :nickname")
    suspend fun getUserByNickname(nickname: String) : User?

    @Query("SELECT * FROM users WHERE account = :account")
    fun getUserByAccount(account: String): LiveData<User>

    @Query("SELECT * FROM users WHERE account = :account")
    suspend fun getUserByAccountUser(account: String) : User?

    @Update
    suspend fun updateUserInfo(user: User): Int

    @Delete
    suspend fun removeUser(user: User) : Int

    @Query("DELETE FROM users WHERE account = :account")
    suspend fun removeUserByAccount(account : String) : Int

    @Query("DELETE FROM users")
    suspend fun removeAllUsers() : Int
}
