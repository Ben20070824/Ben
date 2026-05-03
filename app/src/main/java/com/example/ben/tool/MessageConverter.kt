package com.example.ben.tool

import androidx.room.TypeConverter
import com.example.ben.data.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MessageConverter {
    @TypeConverter
    fun fromMessageList(list: List<Message>) : String{
        return Gson().toJson((list))
    }
    @TypeConverter
    fun toMessageList(json: String) : List<Message>{
        val type =object :TypeToken<List<Message>>() {}.type
        return Gson().fromJson(json, type)
    }
}