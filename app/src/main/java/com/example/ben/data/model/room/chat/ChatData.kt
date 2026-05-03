package com.example.ben.data.model.room.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.ben.data.model.Message
import com.example.ben.tool.MessageConverter

@Entity(tableName = "ChatData")
@TypeConverters(MessageConverter::class)
data class ChatData(
    @PrimaryKey(true)
    val id: Long = 0,
    @ColumnInfo
    val account: String,
    @ColumnInfo
    val chatList: List<Message>,
    @ColumnInfo
    val title: String
)