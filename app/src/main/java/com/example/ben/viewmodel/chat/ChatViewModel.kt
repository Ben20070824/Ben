package com.example.ben.viewmodel.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.MyApplication
import com.example.ben.data.model.Message
import com.example.ben.data.repository.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    private val _messageList = MutableLiveData<MutableList<Message>>(mutableListOf())
    private val _isLoading = MutableLiveData<Boolean>(false)
    private val _model = MutableLiveData("deepseek-v4-flash")

    val messageList: LiveData<MutableList<Message>> = _messageList
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun initChat(chatId: Long) {
        repository.init(chatId)
        // 加载该会话的历史消息
        val chatData = repository.getChatData()
        if (chatData != null) {
            MyApplication.title = chatData.title
            _messageList.value = chatData.chatList.toMutableList()
        }
    }

    fun modifyModel(position: Int) {
        _model.value = when(position) {
            0 -> "deepseek-v4-flash"
            1 -> "deepseek-v4-pro"
            else -> "deepseek-v4-flash"
        }
    }

    fun callAi(message: String){
        _isLoading.value=true
        _messageList.value?.add(Message.MyMessage("user",message)) ?: return
        _messageList.value=_messageList.value
        val model = _model.value ?: return
        val messages = _messageList.value ?: return
        repository.callAi(model,messages){list ->
            _messageList.value = list.toMutableList()
            _isLoading.value=false

            viewModelScope.launch {
                repository.updateChat(list)
            }
        }
    }
}
