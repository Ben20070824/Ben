package com.example.ben.data.model.room.chat

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChatDataDao {
    @Insert
    suspend fun insertChat(chatData: ChatData): Long

    @Update
    suspend fun updateChat(chatData: ChatData): Int

    @Delete
    suspend fun deleteChat(chatData: ChatData): Int

    @Query("SELECT * FROM ChatData WHERE id = :id")
    suspend fun getChatById(id: Long): ChatData?

    @Query("SELECT * FROM CHATDATA WHERE account = :account")
    fun getChatsByAccount(account: String): LiveData<List<ChatData>>
}
