package com.example.ben.viewmodel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.MyApplication
import com.example.ben.data.repository.UserRepository
import com.example.ben.ui.activity.MainActivity
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private var _loginState = MutableLiveData(false)
    private var _toastMsg = MutableLiveData<String>()
    private var _autoLogin = MutableLiveData(false)

    val loginState : LiveData<Boolean> = _loginState
    val toastMsg : LiveData<String> = _toastMsg
    val autoLogin : LiveData<Boolean> = _autoLogin
    fun doLogin(account : String, password: String){
        viewModelScope.launch {
            val user = userRepository.getUserByAccount(account)
            if (user == null) {
                _toastMsg.value = "用户不存在"
                return@launch
            }
            if (user.password == password) {
                _loginState.value = true
                MyApplication.account= account
                userRepository.login(account)
            } else _toastMsg.value = "密码不正确"
        }
    }
    fun autoLogin(){
        val account = userRepository.autoLogin()
        if(account.isNotBlank()){
            MyApplication.account=account
            _autoLogin.value = true
        }
    }
}
