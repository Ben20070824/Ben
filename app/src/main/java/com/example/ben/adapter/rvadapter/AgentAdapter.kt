package com.example.ben.adapter.rvadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ben.data.model.room.agent.AgentData
import com.example.ben.databinding.ItemAgentBinding

class AgentAdapter : ListAdapter<AgentData, AgentAdapter.MyViewHolder>(AgentDiffCallBack()) {
    var onItemClick : ((Int) -> Unit)? = null
    var onButtonClick : ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAgentBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        viewHolder: MyViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        viewHolder.binding.root.setOnClickListener { onItemClick?.invoke(item.id) }
        viewHolder.binding.btnDetail.setOnClickListener { onButtonClick?.invoke(item.id) }
        viewHolder.binding.tvAgentName.text = item.name
        viewHolder.binding.tvAgentDetail.text = item.introduction
    }

    inner class MyViewHolder(val binding: ItemAgentBinding): RecyclerView.ViewHolder(binding.root)

    class AgentDiffCallBack : DiffUtil.ItemCallback<AgentData>() {
        override fun areItemsTheSame(
            oldItem: AgentData,
            newItem: AgentData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AgentData,
            newItem: AgentData
        ): Boolean {
            return oldItem == newItem
        }
    }
}
