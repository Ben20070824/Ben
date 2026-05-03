package com.example.ben.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.MyApplication
import com.example.ben.data.room.UserDataBase
import com.example.ben.data.room.UserDao
import com.example.ben.data.room.User
import kotlinx.coroutines.launch

class EditViewModel : ViewModel() {
    private val userDao : UserDao = UserDataBase.getDatabase().userDao()

    private val _user = MutableLiveData<User?>()
    private val _toastMsg = MutableLiveData<String>()

    val user : LiveData<User?> = _user
    val toastMsg : LiveData<String> = _toastMsg

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            val account = MyApplication.account
            if (account.isNotBlank()) {
                val user = userDao.getUserByAccountUser(account)
                _user.postValue(user)
            } else {
                _user.postValue(null)
            }
        }
    }

    fun editSucceed(account: String, nickname: String, gender: String, age: Int, birth: String, city: String, university: String, signature: String){
        val userModify = user.value
        if (userModify == null) {
            _toastMsg.value = "用户信息加载失败"
            return
        }

        userModify.account = account
        userModify.nickname = nickname
        userModify.gender = gender
        userModify.age = age
        userModify.birth = birth
        userModify.city = city
        userModify.university = university
        userModify.signature = signature

        viewModelScope.launch {
            userDao.updateUserInfo(userModify)
            MyApplication.account = userModify.account
            _toastMsg.value = "编辑成功"
        }
    }
}
