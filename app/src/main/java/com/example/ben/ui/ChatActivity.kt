package com.example.ben.ui

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
import com.example.ben.R
import com.example.ben.adapter.ChatAdapter
import com.example.ben.databinding.ActivityChatBinding
import com.example.ben.viewmodel.ChatViewModel
import kotlinx.coroutines.delay
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
    }

    private fun initView() {
        val id : Long = intent.getLongExtra("id",0)
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
            val wasEmpty = mAdapter.itemCount == 0
            mAdapter.submitList(ArrayList(list)) {
                if (list.isNotEmpty()) {
                    if (wasEmpty || isUserNearBottom()) {
                        binding.rvChat.smoothScrollToPosition(list.size - 1)
                    }
                }
            }
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

    private fun isUserNearBottom(): Boolean {
        val layoutManager = binding.rvChat.layoutManager as LinearLayoutManager
        val lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        return totalItemCount == 0 || lastVisibleItem >= totalItemCount - 3
    }

    private fun initEvent() {
        binding.btnSend.setOnClickListener {
            val content = binding.etMessage.text.toString().trim()
            if (content.isNotBlank()) {
                viewModel.callAi(content)
                binding.etMessage.text.clear()
            } else {
                Toast.makeText(this, "请输入消息内容", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
