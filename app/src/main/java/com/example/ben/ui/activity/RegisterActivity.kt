package com.example.ben.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ben.R
import com.example.ben.data.room.UserDataBase
import com.example.ben.data.room.UserDao
import com.example.ben.databinding.ActivityRegisterBinding
import com.example.ben.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var userDao: UserDao
    private val viewModel : RegisterViewModel by viewModels()
    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initEvent()
        initObserver()
    }

    private fun initObserver() {
        viewModel.apply{
            registerState.observe(this@RegisterActivity) { bool ->
                if (bool == true) {
                    LoginActivity.start(this@RegisterActivity)
                }
            }
            toastMsg.observe(this@RegisterActivity){toastMsg ->
                Toast.makeText(this@RegisterActivity,toastMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initEvent() {
        userDao = UserDataBase.getDatabase().userDao()
        binding.apply{
            btnRegister.setOnClickListener {
                val account = etAccount.text.toString()
                val password = etPassword.text.toString()
                val passwordAgain = etConfirmPassword.text.toString()
                viewModel.doRegister(account,password,passwordAgain,cbAgree.isChecked)
            }
        }
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context,RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}
