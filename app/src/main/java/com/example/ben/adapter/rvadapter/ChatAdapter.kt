package com.example.ben.adapter.rvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ben.data.model.Message
import com.example.ben.databinding.ItemChatAiBinding
import com.example.ben.databinding.ItemChatMeBinding
import com.example.ben.databinding.ItemEmptyBinding

class ChatAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {
    companion object {
        private const val VIEW_TYPE_AI = 0
        private const val VIEW_TYPE_ME = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Message.AiMessage -> VIEW_TYPE_AI
            is Message.MyMessage -> VIEW_TYPE_ME
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_AI -> {
                val binding = ItemChatAiBinding.inflate(inflater, parent, false)
                AiViewHolder(binding)
            }
            VIEW_TYPE_ME -> {
                val binding = ItemChatMeBinding.inflate(inflater, parent, false)
                MeViewHolder(binding)
            }
            else -> {
                val binding = ItemEmptyBinding.inflate(inflater,parent,false)
                EmptyVH(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder) {
            is AiViewHolder -> {
                holder.bind(message as Message.AiMessage)
            }
            is MeViewHolder -> holder.bind(message as Message.MyMessage)
            else -> {}
        }
    }

    inner class AiViewHolder(
        val binding: ItemChatAiBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message.AiMessage) {
            binding.tvMessage.text = message.content
        }
    }

    inner class MeViewHolder(
        val binding: ItemChatMeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message.MyMessage) {
            binding.tvMessage.text = message.content
        }
    }
    // 空白占位VH，无内容
    class EmptyVH(binding: ItemEmptyBinding) : RecyclerView.ViewHolder(binding.root)

    class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }
}