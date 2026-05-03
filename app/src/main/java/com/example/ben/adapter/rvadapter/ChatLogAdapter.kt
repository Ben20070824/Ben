package com.example.ben.adapter.rvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ben.MyApplication
import com.example.ben.data.model.room.chat.ChatData
import com.example.ben.databinding.ItemLogBinding
import com.example.ben.ui.activity.ChatActivity

class ChatLogAdapter : ListAdapter<ChatData, ChatLogAdapter.MyViewHolder>(ChatDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLogBinding.inflate(inflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        viewHolder: MyViewHolder,
        position: Int
    ) {
        viewHolder.onBind(getItem(position))
    }



    inner class MyViewHolder(private val binding: ItemLogBinding) : RecyclerView.ViewHolder(binding.root){

        fun onBind(chatData: ChatData){
            binding.apply {
                tvTitle.text = chatData.title
                root.setOnClickListener {
                    ChatActivity.start(MyApplication.globalContext,chatData.id)
                }
            }
        }
    }
    class ChatDiffCallback : DiffUtil.ItemCallback<ChatData>(){
        override fun areItemsTheSame(
            p0: ChatData,
            p1: ChatData
        ): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(
            p0: ChatData,
            p1: ChatData
        ): Boolean {
            return p0 == p1
        }
    }
}