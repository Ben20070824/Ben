package com.example.ben.ui.fragment

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ben.adapter.rvadapter.AgentAdapter
import com.example.ben.databinding.FragmentAgentBinding
import com.example.ben.ui.activity.AgentActivity
import com.example.ben.ui.activity.AgentEditActivity
import com.example.ben.viewmodel.agent.AgentViewModel

class AgentFragment : Fragment() {
    private val viewModel = AgentViewModel()
    private var _binding : FragmentAgentBinding? = null
    private val binding
        get() = _binding!!
    private val myAdapter = AgentAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAgentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObserver()
        initEvent()
    }

    private fun initView() {
        binding.tvAgent.typeface = Typeface.createFromAsset(requireContext().assets, "FZSTK.TTF")
        binding.rvAgent.apply {
            myAdapter.onItemClick = { id ->
                AgentActivity.start(requireContext(), id)
            }
            myAdapter.onButtonClick = { id->
                AgentEditActivity.startFromChat(requireContext(),id)
            }
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObserver() {
        viewModel.list.observe(viewLifecycleOwner) { list ->
            myAdapter.submitList(list)
        }
    }

    private fun initEvent() {
        binding.btnAdd.setOnClickListener {
            AgentEditActivity.start(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
