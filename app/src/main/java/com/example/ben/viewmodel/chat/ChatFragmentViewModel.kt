package com.example.ben.viewmodel.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.MyApplication
import com.example.ben.data.model.room.chat.ChatData
import com.example.ben.data.respository.ChatRepository
import kotlinx.coroutines.launch

class ChatFragmentViewModel : ViewModel() {
    private val chatRepository = ChatRepository()
    val list: LiveData<List<ChatData>> =chatRepository.getChatsByAccount(MyApplication.account)



    fun insertChat(chatData: ChatData) {
        viewModelScope.launch {
            chatRepository.insertChat(chatData)
        }
    }
}
