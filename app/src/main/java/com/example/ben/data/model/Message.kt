package com.example.ben.data.model

sealed class Message {
    data class MyMessage(val id: Int, val message: String) : Message()
    data class AiMessage(val id: Int, val message: String) : Message()
}
