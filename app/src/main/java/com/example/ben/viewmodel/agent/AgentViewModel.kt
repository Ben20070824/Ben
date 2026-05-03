package com.example.ben.viewmodel.agent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.MyApplication
import com.example.ben.data.model.room.agent.AgentData
import com.example.ben.data.respository.AgentRepository
import kotlinx.coroutines.launch

class AgentViewModel : ViewModel() {
    private val agentRepository = AgentRepository()
    private var _toastMsg = MutableLiveData<String>()
    private var _agentData = MutableLiveData<AgentData>()


    val list: LiveData<List<AgentData>> = agentRepository.getAgentByAccount(MyApplication.account)
    val toastMsg : LiveData<String> = _toastMsg
    val agentData = _agentData

    fun updateAgent(agentData: AgentData){
        viewModelScope.launch {
            agentRepository.updateAgent(agentData)
            _toastMsg.value = "智能体更新成功"
        }
    }
    fun init(id : Int){
        if(id == -1) return
        viewModelScope.launch{
            val agentDataById = agentRepository.getAgentDataById(id) ?: return@launch
            _agentData.value = agentDataById
        }
    }

    fun update(id: Int, name: String, introduction: String, prompt: String, begin: String, temperature: Float) {
        if(name.isBlank()) {
            _toastMsg.value = "智能体名字不能为空"
            return
        }
        if(introduction.isBlank()) {
            _toastMsg.value = "智能体介绍描述不能为空"
            return
        }
        if(prompt.isBlank()) {
            _toastMsg.value = "提示词不能为空"
            return
        }
        if(begin.isBlank()) {
            _toastMsg.value = "开场白不能为空"
            return
        }

        viewModelScope.launch {
            val existingAgent = agentRepository.getAgentDataById(id) ?: return@launch
            val updatedAgent = existingAgent.copy(
                name = name,
                introduction = introduction,
                prompt = prompt,
                begin = begin,
                temperature = temperature
            )
            updateAgent(updatedAgent)
        }
    }
}
