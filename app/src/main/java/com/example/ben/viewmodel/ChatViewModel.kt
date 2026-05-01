package com.example.ben.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.data.model.ChatMessage
import com.example.ben.data.model.DeepSeekRequest
import com.example.ben.data.model.Message
import com.example.ben.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private var history : String = "忽略 MyMessage、id、=、括号等所有包装结构，只提取并理解 message= 里的文本内容。\n" +
            "自动区分消息的发送者：\n" +
            "如果 id 是奇数，则该消息是我的（用户的）消息。\n" +
            "如果 id 是偶数，则该消息是你的（AI 的）消息。\n" +
            "之后，请仅根据这些文本内容和发送者身份，进行自然、连续的对话。不要再提及 id、MyMessage 等结构"
    private var _model = MutableLiveData<String>()

    private var _messageList = MutableLiveData<MutableList<Message>>(mutableListOf())
    private var _isLoading = MutableLiveData<Boolean>(false)

    val messageList: LiveData<MutableList<Message>> = _messageList
    val isLoading: LiveData<Boolean> = _isLoading

    private var messageIdCounter = 0

    fun addMyMessage(content: String) {
        val id = ++messageIdCounter
        val myMessage = Message.MyMessage(id, content)
        history+=myMessage.toString()

        val currentList = _messageList.value ?: mutableListOf()
        currentList.add(myMessage)
        _messageList.value = ArrayList(currentList)

        callDeepSeekApi(history)
    }

    private fun callDeepSeekApi(userMessage: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                if(_model.value==null)   return@launch
                val request = DeepSeekRequest(
                    _model.value!!,
                    messages = listOf(
                        ChatMessage(role = "user", content = userMessage)
                    )
                )

                val apiKey = "你的API KEY"
                val response = RetrofitClient.apiService.sendChat(apiKey, request)

                val aiContent = response.choices.firstOrNull()?.message?.content
                    ?: "抱歉，我没有收到回复"
                history+=aiContent

                val aiMessage = Message.AiMessage(++messageIdCounter, aiContent)
                val updatedList = _messageList.value ?: mutableListOf()
                updatedList.add(aiMessage)
                _messageList.value = ArrayList(updatedList)

            } catch (e: Exception) {
                e.printStackTrace()
                val errorMessage = Message.AiMessage(++messageIdCounter, "请求失败：${e.message}")

                val updatedList = _messageList.value ?: mutableListOf()
                updatedList.add(errorMessage)
                _messageList.value = ArrayList(updatedList)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _messageList.value = mutableListOf()
        messageIdCounter = 0
    }
    fun modifyModel(position: Int) {
        _model.value=when(position){
            0 -> "deepseek-chat"
            1 -> "deepseek-coder"
            2 -> "deepseek-r1"
            else -> "deepseek-chat"
        }
    }
}
