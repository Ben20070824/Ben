package com.example.ben.viewmodel.agent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.data.model.Message
import com.example.ben.data.model.room.agent.AgentData
import com.example.ben.data.repository.AgentRepository
import kotlinx.coroutines.launch

class AgentChatViewModel : ViewModel() {
    private val repository = AgentRepository()
    private lateinit var agentData: AgentData
    private var _name = MutableLiveData<String>()
    private var _begin = MutableLiveData<String>()
    private var _list = MutableLiveData<List<Message>>()
    private var _model = MutableLiveData<String>()
    private var _isThinking = MutableLiveData(false)
    private var temperature : Float = 0.7f

    val list : LiveData<List<Message>> = _list
    val model : LiveData<String> = _model
    val name : LiveData<String> = _name
    val begin : LiveData<String> = _begin
    val isThinking : LiveData<Boolean> = _isThinking

    fun init(id : Int){
        viewModelScope.launch {
            agentData= repository.getAgentDataById(id) ?: return@launch
            val chatList = agentData.chatList.toMutableList()
            val introduction = agentData.introduction
            val prompt = agentData.prompt
            _name.value = agentData.name
            _begin.value = agentData.begin

            val systemMessage = Message.SystemMessage(
                role = "system",
                content = introduction + prompt
            )

            chatList.add(systemMessage)
            temperature = agentData.temperature
            _list.value = chatList
        }
    }

    fun modifyModel(position: Int) {
        _model.value = when(position) {
            0 -> "deepseek-v4-flash"
            1 -> "deepseek-v4-pro"
            else -> "deepseek-v4-flash"
        }
    }

    fun addMessage(content: String){
        val message = Message.MyMessage("user", content)
        val messages = _list.value?.toMutableList() ?: ArrayList()
        messages.add(message)
        _list.value = messages
        callAi()
    }

    fun callAi(){
        val currentModel = _model.value ?: "deepseek-v4-flash"
        val currentList = _list.value ?: return
        _isThinking.value=true
        repository.callAi(currentModel, currentList, temperature){list->
            _list.value = list
            _isThinking.value = false
            viewModelScope.launch{
                repository.updateAgent(agentData.copy(chatList = list))
            }
        }
    }
}