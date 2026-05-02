package com.example.ben.data.model
data class DeepSeekRequest(
    val model: String = "deepseek-v4-pro",
    val messages: List<Message>,
    val reason_effort: String = "high",
    val max_tokens :Int = 10000,
    val stream: Boolean = false
)

//往后可以试试用流式输出，现在水平不够
data class StreamOptions(
    val include_usage: Boolean = true
)
sealed class Message{
    data class MyMessage(
        val role: String,
        val content: String
    ) : Message()

    data class AiMessage(
        val role: String?,
        val content: String?,
        val reasoning_content: String?
    ) : Message()
}
data class DeepSeekResponse(
    val id: String?,//唯一标识AI对话，后续删除对话的功能可以用这个
    val created: Long?,//创建的时间戳
    val model: String?,
    val system_fingerprint: String?,
    val choices: List<Choice>?,
    val usage: Usage?
)
data class Choice(
    val index: Int?,
    val message: Message.AiMessage?,
    val finish_reason: String?
)
// token消耗,暂时用不上
data class Usage(
    val prompt_tokens: Int?,
    val completion_tokens: Int?,
    val total_tokens: Int?
)