package com.example.ben.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ben.data.model.Message
import com.example.ben.data.respository.ChatRepository

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    private val _messageList = MutableLiveData<MutableList<Message>>(mutableListOf())
    private val _isLoading = MutableLiveData<Boolean>(false)
    private val _model = MutableLiveData("deepseek-v4-flash")

    val messageList: LiveData<MutableList<Message>> = _messageList
    val isLoading: LiveData<Boolean> = _isLoading
    private var messageIdCounter = 0
    suspend fun update(){
        if(_messageList.value==null) return
        repository.updateChat(_messageList.value!!.toList())
    }
    suspend fun insert(){
        repository.insert()
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
        }
    }
}
