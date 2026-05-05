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
import com.example.ben.databinding.ActivityLoginBinding
import com.example.ben.viewmodel.user.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel : LoginViewModel by viewModels()
    private val binding by lazy {
         ActivityLoginBinding.inflate(layoutInflater)
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
        viewModel.apply {
            loginState.observe(this@LoginActivity) {bool ->
                if(bool){
                    MainActivity.start(this@LoginActivity)
                }
            }
            toastMsg.observe(this@LoginActivity) {toastMsg ->
                Toast.makeText(this@LoginActivity,toastMsg, Toast.LENGTH_SHORT).show()
            }
            autoLogin.observe(this@LoginActivity){autoLogin->
                if(autoLogin) MainActivity.start(this@LoginActivity)
                else return@observe
            }
        }
    }

    private fun initEvent() {
        viewModel.autoLogin()
        binding.apply {
            btnRegister.setOnClickListener {
                RegisterActivity.start(this@LoginActivity)
            }
            btnLogin.setOnClickListener {
                viewModel.doLogin(etAccount.text.toString(),etPassword.text.toString())
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}
