package com.example.ben.data.repository

import androidx.lifecycle.LiveData
import com.example.ben.BuildConfig
import com.example.ben.data.model.DeepSeekRequest
import com.example.ben.data.model.DeepSeekResponse
import com.example.ben.data.model.Message
import com.example.ben.data.model.room.agent.AgentData
import com.example.ben.data.model.room.agent.AgentDataBase
import com.example.ben.data.remote.RetrofitClient
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AgentRepository {
    private val database = AgentDataBase.getDataBase()
    private val agentDataDao = database.agentDataDao()

    suspend fun insertAgent(agentData: AgentData){
        agentDataDao.insertAgent(agentData)
    }
    suspend fun updateAgent(agentData: AgentData){
        agentDataDao.updateAgent(agentData)
    }
    fun getAgentByAccount(account: String): LiveData<List<AgentData>>{
        return agentDataDao.getAgentByAccount(account)
    }
    suspend fun getAgentDataById(id : Int) : AgentData?{
        return agentDataDao.getAgentById(id)
    }
    suspend fun getAgentsDataByAccount(account: String) : List<AgentData>?{
        return agentDataDao.getAgentsByAccount(account)
    }

    fun callAi(model: String,messageList: List<Message>,temperature : Float,callback: (List<Message>) -> Unit){
        val messages = messageList.toMutableList()
        val deepSeekRequest = DeepSeekRequest(model = model, messages = messageList,temperature = temperature)
        RetrofitClient.request.sendChat(BuildConfig.API_KEY,deepSeekRequest)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<DeepSeekResponse> {
                private lateinit var disposable: Disposable
                override fun onSubscribe(d: Disposable) {
                    disposable = d
                }
                override fun onNext(response: DeepSeekResponse) {
                    val aiMessage = response.choices?.firstOrNull()?.message
                    if (aiMessage != null) {
                        messages.add(Message.AiMessage(
                            role = aiMessage.role,
                            content = aiMessage.content,
                            reasoning_content = aiMessage.reasoning_content
                        ))
                    }
                    callback(messages)
                }
                override fun onComplete() {

                }
                override fun onError(e: Throwable) {
                    val errorMessage = Message.AiMessage("assistant",e.message ?:"未知错误，请联系开发人员","")
                    messages.add(errorMessage)
                    callback(messages)
                }
            })
    }
}