package com.example.ben.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.ben.R
import com.example.ben.adapter.ViewPagerAdapter
import com.example.ben.databinding.ActivityMainBinding
import com.example.ben.ui.fragment.AgentFragment
import com.example.ben.ui.fragment.ChatFragment
import com.example.ben.ui.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
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
    }

    private fun initView() {
        val fragments = listOf({ChatFragment()}, {AgentFragment()}, {ProfileFragment()})
        binding.viewPager.apply {
            adapter= ViewPagerAdapter(this@MainActivity,fragments)
            offscreenPageLimit=2
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when(position){
                        0-> binding.bottom.selectedItemId=R.id.navigation_chat
                        1-> binding.bottom.selectedItemId=R.id.navigation_agents
                        2-> binding.bottom.selectedItemId=R.id.navigation_profile
                    }
                }
            })
        }
        binding.bottom.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_chat -> binding.viewPager.currentItem=0
                R.id.navigation_agents -> binding.viewPager.currentItem=1
                R.id.navigation_profile -> binding.viewPager.currentItem=2
                else -> throw Exception("bottomNavigation未知ID错误")
            }
            true
        }
    }
    companion object{
        fun start(context: Context){
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}