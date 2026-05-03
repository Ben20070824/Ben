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

class ProfileViewModel : ViewModel() {
    private val userDao : UserDao = UserDataBase.getDatabase().userDao()
    private val _user = MutableLiveData<User?>()

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

    val user : LiveData<User?> = _user
}
