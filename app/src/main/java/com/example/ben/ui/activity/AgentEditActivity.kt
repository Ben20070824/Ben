package com.example.ben.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ben.R
import com.example.ben.databinding.ActivityAgentEditBinding
import com.example.ben.viewmodel.agent.AgentViewModel

class AgentEditActivity : AppCompatActivity() {
    private val viewModel: AgentViewModel by viewModels()
    private val binding by lazy { ActivityAgentEditBinding.inflate(layoutInflater) }

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
        viewModel.init(id)
        binding.seekbarTemperature.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val temperature = progress / 50.0f
                binding.tvTemperatureValue.text = String.format("%.1f", temperature)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        binding.btnSave.setOnClickListener {
            val name = binding.etAgentName.text.toString().trim()
            val introduction = binding.etAgentDescription.text.toString().trim()
            val prompt = binding.etSystemPrompt.text.toString().trim()
            val begin = binding.etGreeting.text.toString().trim()
            val temperature = binding.seekbarTemperature.progress / 50.0f

            viewModel.update(id, name, introduction, prompt, begin, temperature)
            finish()
        }
        binding.btnCancel.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("提示")
            builder.setMessage("确定要取消编辑吗？未保存的内容将会丢失")
            builder.setPositiveButton("确定") { _, _ ->
                finish()
            }
            builder.setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            builder.create().show()
        }
        binding.btnChangeAvatar.setOnClickListener {
            Toast.makeText(this, "功能尚未开发，敬请期待", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initObserver() {
        viewModel.toastMsg.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        viewModel.agentData.observe(this){data ->
            binding.apply {
                etAgentName.setText(data.name)
                etAgentDescription.setText(data.introduction)
                etGreeting.setText(data.begin)
                etSystemPrompt.setText(data.prompt)
                seekbarTemperature.progress = (data.temperature*50).toInt().coerceIn(0,100)
            }
        }
    }

    companion object {
        private var id: Int = -1

        fun start(context: Context, id: Int) {
            this.id = id
            val intent = Intent(context, AgentEditActivity::class.java)
            context.startActivity(intent)
        }
    }
}
