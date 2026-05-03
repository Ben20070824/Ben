package com.example.ben.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.MyApplication
import com.example.ben.data.room.UserDataBase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val userDao by lazy {
        UserDataBase.getDatabase().userDao()
    }
    var _loginState = MutableLiveData(false)
    var _toastMsg = MutableLiveData<String>()

    val loginState : LiveData<Boolean> = _loginState
    val toastMsg : LiveData<String> = _toastMsg

    fun doLogin(account : String, password: String){
        viewModelScope.launch {
            val user = userDao.getUserByAccountUser(account)
            if (user == null) {
                _toastMsg.value = "用户不存在"
                return@launch
            }
            if (user.password == password) {
                _loginState.value = true
                MyApplication.account= account
            } else _toastMsg.value = "密码不正确"
        }
    }
}
