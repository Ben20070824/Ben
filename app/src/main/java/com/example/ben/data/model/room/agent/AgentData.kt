package com.example.ben.data.model.room.agent

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ben.data.model.Message

@Entity("AgentData")
data class AgentData(
    @PrimaryKey(true)
    val id : Int=0,
    val account: String,
    val name: String,
    val introduction: String,
    val prompt: String,
    val begin: String,
    val chatList: List<Message>,
    val temperature: Float = 0.7f)