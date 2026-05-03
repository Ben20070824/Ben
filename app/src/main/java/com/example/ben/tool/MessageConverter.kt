package com.example.ben.tool

import androidx.room.TypeConverter
import com.example.ben.data.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MessageConverter {

    @TypeConverter
    fun fromMessageList(list: List<Message>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toMessageList(json: String): List<Message> {
        if (json.isBlank()) return emptyList()

        val gson = Gson()
        val messageList = mutableListOf<Message>()

        // 把json转成数组
        val jsonElements = gson.fromJson(json, Array<com.google.gson.JsonElement>::class.java)

        for (element in jsonElements) {
            val obj = element.asJsonObject
            val role = obj.get("role")?.asString

                        val msg = when (role) {
                "user" -> gson.fromJson(obj, Message.MyMessage::class.java)
                "system" -> gson.fromJson(obj, Message.SystemMessage::class.java)
                else -> gson.fromJson(obj, Message.AiMessage::class.java)
            }

            messageList.add(msg)
        }
        return messageList
    }
}