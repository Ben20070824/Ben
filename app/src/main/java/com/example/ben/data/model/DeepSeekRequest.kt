package com.example.ben.data.model


data class DeepSeekRequest(
    val model: String = "deepseek-coder",
    val messages: List<ChatMessage>
)

data class ChatMessage(
    val role: String,
    val content: String
)

data class DeepSeekResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: AssistantMessage
)

data class AssistantMessage(
    val role: String,
    val content: String
)
