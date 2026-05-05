package com.example.ben.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ben.R
import com.example.ben.adapter.rvadapter.ChatAdapter
import com.example.ben.databinding.ActivityAgentBinding
import com.example.ben.viewmodel.agent.AgentChatViewModel

class AgentActivity : AppCompatActivity() {
    private val binding: ActivityAgentBinding by lazy { ActivityAgentBinding.inflate(layoutInflater) }
    private val viewModel: AgentChatViewModel by viewModels()
    private val myAdapter = ChatAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
        initEvent()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.init(id)
    }

    private fun initView() {
        binding.rvAgent.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(this@AgentActivity).apply {
                reverseLayout = false
                stackFromEnd = false
            }
        }
        val models = arrayOf("deepseek-v4-flash 快速响应", "deepseek-v4-pro 复杂任务")
        val spAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, models)
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spModel.adapter = spAdapter
        binding.spModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                viewModel.modifyModel(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.modifyModel(0)
            }
        }
    }

    private fun initObserver() {
        viewModel.list.observe(this) { list ->
            myAdapter.submitList(list)
            if (list.size == 0) return@observe
            binding.rvAgent.smoothScrollToPosition(list.size-1)
            Log.d("ljh","滑动成功")
        }
        viewModel.name.observe(this){name->
            binding.tvAi.text=name
        }
        viewModel.begin.observe(this){begin->
            binding.tvBegin.text=begin
        }
        viewModel.isThinking.observe(this){isThink->
            if(isThink)  binding.etMessage.hint = "我正在思考哦"
            else binding.etMessage.hint="快来给我发送信息吧~~~"
        }
    }

    private fun initEvent() {
        binding.btnMenu.setOnClickListener {
            finish()
        }
        binding.btnEdit.setOnClickListener {
            AgentEditActivity.start(this, id)
        }
        binding.btnSend.setOnClickListener {
            val content = binding.etMessage.text.toString().trim()
            if (content.isBlank()) {
                return@setOnClickListener
            }
            viewModel.addMessage(content)
            binding.etMessage.text.clear()
        }
    }

    companion object {
        private var id: Int = -1
        fun start(context: Context, id: Int) {
            this.id = id
            val intent = Intent(context, AgentActivity::class.java)
            context.startActivity(intent)
        }
    }
}