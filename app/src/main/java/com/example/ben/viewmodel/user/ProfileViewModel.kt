package com.example.ben.viewmodel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ben.MyApplication
import com.example.ben.data.model.room.user.User
import com.example.ben.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = UserRepository()
    private val _user = MutableLiveData<User?>()

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            val account = MyApplication.account
            if (account.isNotBlank()) {
                val user = repository.getUserByAccount(account)
                _user.postValue(user)
            } else {
                _user.postValue(null)
            }
        }
    }
    val user : LiveData<User?> = _user
    fun unLogin(){
        repository.unLogin()
    }
}
