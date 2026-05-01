package com.example.ben.data.remote

import com.example.ben.data.model.DeepSeekRequest
import com.example.ben.data.model.DeepSeekResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("v1/chat/completions")
    suspend fun sendChat(
        @Header("Authorization") token: String,
        @Body request: DeepSeekRequest
    ): DeepSeekResponse
}
