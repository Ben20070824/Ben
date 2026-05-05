package com.example.ben.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ben.R
import com.example.ben.databinding.ActivityEditBinding
import com.example.ben.viewmodel.user.EditViewModel

class EditActivity : AppCompatActivity() {
    private val binding by lazy { ActivityEditBinding.inflate(layoutInflater) }
    private val viewModel: EditViewModel by viewModels()

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
    private fun initEvent() {
        binding.btnChange.setOnClickListener {
            Toast.makeText(this,"更多功能，敬请期待！", Toast.LENGTH_SHORT).show()
        }
        binding.etAccountText.setOnClickListener {
            Toast.makeText(this,"账号是唯一标识，暂不支持修改~~~", Toast.LENGTH_SHORT).show()
        }
        binding.btnEdit.setOnClickListener {
            val account = binding.etAccountText.text.toString().trim()
            val nickname = binding.etNickName.text.toString().trim()
            val gender = binding.etGender.text.toString().trim()

            if (account.isBlank()) {
                Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val ageText = binding.etAge.text.toString().trim()
            if (ageText.isBlank()) {
                Toast.makeText(this, "年龄不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = try {
                ageText.toInt()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "请输入有效的年龄", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val birth = binding.etBirthText.text.toString().trim()
            val city = binding.etCityText.text.toString().trim()
            val university = binding.etUniversityText.text.toString().trim()
            val signature = binding.etSignatureText.text.toString().trim()

            viewModel.updateUser(account, nickname, gender, age, birth, city, university, signature)
        }

        binding.btnBack.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("提示")
            builder.setMessage("信息还没有保存，确定要退出吗")
            builder.setPositiveButton("确定"){_,_ ->
                this.finish()
            }
            builder.setNegativeButton("取消"){dialog,_ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
    }

    private fun initObserver() {
        viewModel.user.observe(this) { user ->
            if (user == null) {
                Toast.makeText(this, "无法加载用户信息", Toast.LENGTH_SHORT).show()
                finish()
                return@observe
            }
            binding.apply {
                etAccountText.setText(user.account)
                etNickName.setText(user.nickname)
                etGender.setText(user.gender)
                etAge.setText(user.age.toString())
                etBirthText.setText(user.birth)
                etCityText.setText(user.city)
                etUniversityText.setText(user.university)
                etSignatureText.setText(user.signature)
            }
        }

        viewModel.toastMsg.observe(this) { toastMsg ->
            if (!toastMsg.isNullOrBlank()) {
                Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show()
                if (toastMsg == "编辑成功") {
                    finish()
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, EditActivity::class.java)
            context.startActivity(intent)
        }
    }
}
