package com.example.ben.data.repository

import androidx.lifecycle.LiveData
import com.example.ben.BuildConfig
import com.example.ben.MyApplication
import com.example.ben.data.model.room.chat.ChatData
import com.example.ben.data.model.room.chat.ChatDataBaseClient
import com.example.ben.data.model.DeepSeekRequest
import com.example.ben.data.model.DeepSeekResponse
import com.example.ben.data.model.Message
import com.example.ben.data.remote.RetrofitClient
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class ChatRepository {
    private val chatDataDao =
        ChatDataBaseClient.getDatabase(MyApplication.globalContext).chatDataDao()
    private lateinit var chatData: ChatData
    private var id: Long = -1

    fun getChatData(): ChatData? {
        return if (::chatData.isInitialized) chatData else null
    }

    suspend fun init(id: Long) {
        this.id = id
        chatData = chatDataDao.getChatById(id) ?: return
    }

    suspend fun updateChat(list: List<Message>) {
        if (::chatData.isInitialized) {
            chatDataDao.updateChat(ChatData(id, MyApplication.account, list, MyApplication.title))
        }
    }

    suspend fun insertChat(chatData: ChatData) {
        chatDataDao.insertChat(chatData)
    }

    fun getChatsByAccount(account: String) : LiveData<List<ChatData>>{
        return chatDataDao.getChatsByAccount(account)
    }

    fun callAi(model: String,messageList: List<Message>,callback: (List<Message>) -> Unit){
        val messages = messageList.toMutableList()
        val deepSeekRequest = DeepSeekRequest(model = model, messages = messageList)
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
                    disposable.dispose()
                }
                override fun onError(e: Throwable) {
                    val errorMessage = Message.AiMessage("assistant",e.message ?:"未知错误，请联系开发人员","")
                    messages.add(errorMessage)
                    callback(messages)
                }
            })

    }
}
