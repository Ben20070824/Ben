package com.example.ben.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ben.MyApplication
import com.example.ben.adapter.rvadapter.ChatLogAdapter
import com.example.ben.data.model.room.chat.ChatData
import com.example.ben.databinding.FragmentChatBinding
import com.example.ben.viewmodel.chat.ChatFragmentViewModel

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel :ChatFragmentViewModel by viewModels()
    private lateinit var myAdapter: ChatLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        initEvent()
    }
    private fun initEvent() {
        binding.btnNew.setOnClickListener {
            showCreateChatDialog()
        }
        binding.btnScan.setOnClickListener {
            Toast.makeText(requireContext(),"功能尚待开发", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCreateChatDialog() {
        val editText = EditText(requireContext())
        editText.hint = "请输入对话标题"
        editText.setPadding(50, 20, 50, 20)
        editText.textSize = 16f

        AlertDialog.Builder(requireContext())
            .setTitle("新建对话")
            .setMessage("请输入对话标题：")
            .setView(editText)
            .setPositiveButton("确定") { _, _ ->
                val title = editText.text.toString().trim()
                if (title.isBlank()) {
                    Toast.makeText(requireContext(), "标题不能为空", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                createNewChat(title)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun createNewChat(title: String) {
        val newChat = ChatData(
            account = MyApplication.account,
            chatList = emptyList(),
            title = title
        )

        viewModel.insertChat(newChat)
        Toast.makeText(requireContext(), "对话创建成功", Toast.LENGTH_SHORT).show()
    }

    private fun initObserver() {
        viewModel.list.observe(viewLifecycleOwner) { list ->
            myAdapter.submitList(list)
        }
    }

    private fun initView() {
        myAdapter= ChatLogAdapter()
        binding.rvChatLog.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myAdapter
        }
    }
}
