package com.example.ben.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ben.adapter.rvadapter.ChatAdapter
import com.example.ben.databinding.ActivityChatBinding
import com.example.ben.viewmodel.chat.ChatViewModel
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private val binding: ActivityChatBinding by lazy { ActivityChatBinding.inflate(layoutInflater) }
    private val viewModel: ChatViewModel by viewModels()
    private val mAdapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        initView()
        initObserver()
        initEvent()
        initData()
    }

    private fun initData() {
        if (id > 0) {
            lifecycleScope.launch {
                viewModel.initChat(id)
            }
        }
    }



    private fun initView() {
        binding.apply {
            tvAi.typeface = Typeface.createFromAsset(assets, "FZSTK.TTF")
            rvChat.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(this@ChatActivity).apply {
                    reverseLayout = false
                    stackFromEnd = false
                }
            }
        }
        val models =arrayOf("deepseek-v4-flash 快速响应","deepseek-v4-pro 复杂任务")
        val spAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,models)
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spModel.adapter=spAdapter
        binding.spModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
        viewModel.messageList.observe(this) { list ->
            mAdapter.submitList(ArrayList(list))
            if(list.size==0) return@observe
            binding.rvChat.smoothScrollToPosition(list.size-1)
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.btnSend.isEnabled = !loading
            if (loading) {
                binding.etMessage.hint = "AI 正在思考..."
            } else {
                binding.etMessage.hint = "快来发送你的消息吧~~~~~~"
            }
        }
    }


    private fun initEvent() {
        binding.btnSend.setOnClickListener {
            val content = binding.etMessage.text.toString().trim()
            if (content.isNotBlank()) {
                viewModel.callAi(content,System.currentTimeMillis())
                binding.etMessage.text.clear()
            } else {
                Toast.makeText(this, "请输入消息内容", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnMenu.setOnClickListener {
            this.finish()
        }
    }

    companion object{
        private var id: Long = -1
        fun start(context: Context,id: Long){
            this.id = id
            val intent = Intent(context, ChatActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
