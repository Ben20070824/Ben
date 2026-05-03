package com.example.ben.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ben.ui.activity.MainActivity

class ViewPagerAdapter(val activity: MainActivity,private val fragments: List<() -> Fragment>) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return fragments[position]()
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

}