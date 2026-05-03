package com.example.ben.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.data.room.UserDataBase
import com.example.ben.data.room.UserDao
import com.example.ben.data.room.User
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val userDao : UserDao by lazy { UserDataBase.getDatabase().userDao()  }
    private var _registerState = MutableLiveData<Boolean>()
    private var _toastMsg = MutableLiveData<String>()

    val registerState : LiveData<Boolean> = _registerState
    val toastMsg : LiveData<String> = _toastMsg

    fun doRegister(account: String, password : String,passwordAgain: String,isAgree : Boolean){
        if(account.isBlank()) {
            _toastMsg.value = "账号不能为空"
            return
        }
        if(password.isBlank()){
            _toastMsg.value = "密码不能为空"
            return
        }
        if(password != passwordAgain){
            _toastMsg.value = "两次密码不一致"
            return
        }
        if(!isAgree){
            _toastMsg.value = "未同意用户协议"
            return
        }
        viewModelScope.launch {
            val user = userDao.getUserByAccount(account)
            if(user.value==null){
                userDao.insertUser(User(account=account, password = password))
                val user = userDao.getUserByAccountUser(account)
                if(user!=null){
                    Log.d("Ben","注册成功"+user.account)
                } else Log.d("Ben","注册失败")
                _registerState.value=true
            } else {
                _toastMsg.value = "用户已存在"
                _registerState.value=false
            }
        }
    }
}
