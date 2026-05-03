package com.example.ben.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.ben.databinding.FragmentProfileBinding
import com.example.ben.viewmodel.ProfileViewModel
import com.example.ben.ui.activity.EditActivity

class ProfileFragment : Fragment(){
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initObserver()
    }

    private fun initEvent() {
        binding.btnEdit.setOnClickListener {
            EditActivity.start(requireContext())
        }
        binding.btnLogout.setOnClickListener {
            requireActivity().finish()
        }
        binding.btnMore.setOnClickListener {
        }
    }

    private fun initObserver() {
        viewModel.user.observe(viewLifecycleOwner){user ->
            if (user == null) {
                binding.apply {
                    tvNickName.text = "未登录"
                    tvGender.text = "-"
                    tvAge.text = "-"
                    tvAccountText.text = "-"
                    tvBirthText.text = "-"
                    tvCityText.text = "-"
                    tvUniversityText.text = "-"
                    tvSignatureText.text = "暂无个性签名"
                }
                return@observe
            }
            binding.apply{
                tvNickName.text = user.nickname
                tvGender.text = user.gender
                tvAge.text = user.age.toString()
                tvAccountText.text = user.account
                tvBirthText.text = user.birth
                tvCityText.text = user.city
                tvUniversityText.text = user.university
                tvSignatureText.text = user.signature
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadUser()
    }

    companion object{
        fun newInstance() : ProfileFragment{
            return ProfileFragment()
        }
    }
}
