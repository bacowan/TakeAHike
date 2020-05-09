package com.example.takeahike.interaction

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.takeahike.ui.CurrentHike
import com.example.takeahike.ui.EditRoutesList

class StartScreenPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            CurrentHike()
        }
        else {
            EditRoutesList()
        }
    }
}