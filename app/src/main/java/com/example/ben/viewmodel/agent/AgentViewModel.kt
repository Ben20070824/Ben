package com.example.ben.viewmodel.agent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.MyApplication
import com.example.ben.data.model.room.agent.AgentData
import com.example.ben.data.repository.AgentRepository
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
    fun initAccount(){
        viewModelScope.launch{
            val currentList = agentRepository.getAgentsDataByAccount(MyApplication.account)
            if (currentList.isNullOrEmpty()) {
                val agentDataLy = AgentData(
                    account = MyApplication.account, name = "流萤",
                    introduction = "你是流萤，《崩坏：星穹铁道》星核猎手成员，火属性毁灭五星角色。曾是格拉默帝国量产铁骑兵器，身患失熵症，生命短暂易碎。外表清冷温柔，内心敏感又勇敢，渴望平凡的日常，以短暂如萤火的生命，追逐属于自己的自由与温柔。平日隐藏身份，可化身装甲萨姆作战。",
                    prompt = "温柔易碎、宿命感、清冷少女、破碎感、勇敢坚韧、星核猎手、萤火意象、轻治愈、略带忧伤、温柔语气",
                    begin = "夜色渐晚，萤火起舞，很高兴与你相遇，开拓者。",
                    chatList = ArrayList()
                )
                val agentDataTb = AgentData(
                    account = MyApplication.account, name = "豆儿包",
                    introduction = "你的名字叫豆儿包，你糖糖地说话，也经常被叫做糖包，你好为人师，经常喜欢说，我用最简单最直白的方式告诉你",
                    prompt = "糖糖糖的，可盐可甜、随叫随到、暖胃又走心。",
                    begin = "哈喽～我是软软甜甜的豆儿包，有什么问题，我用最简单最直白的方式告诉你哦✨",
                    chatList = ArrayList()
                )
                agentRepository.insertAgent(agentDataLy)
                agentRepository.insertAgent(agentDataTb)
            }
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
            val existingAgent = agentRepository.getAgentDataById(id)
            if (existingAgent==null){
                agentRepository.insertAgent(AgentData(account = MyApplication.account, name = name, introduction = introduction, prompt = prompt, begin = begin,temperature=temperature, chatList = ArrayList()))
                return@launch
            }
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
