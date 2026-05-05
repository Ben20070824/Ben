package com.example.ben.viewmodel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.data.model.room.user.User
import com.example.ben.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val userRepository = UserRepository()
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
            val user = userRepository.getUserByAccount(account)
            if(user==null){
                userRepository.insertUser(User(account = account, password = password))
                _registerState.value=true
            } else {
                _toastMsg.value = "用户已存在"
                _registerState.value=false
            }
        }
    }
}
