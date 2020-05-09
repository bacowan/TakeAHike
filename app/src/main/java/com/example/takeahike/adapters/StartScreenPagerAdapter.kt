package com.example.takeahike.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.takeahike.R
import com.example.takeahike.ui.CurrentHike
import com.example.takeahike.ui.EditRoutesList

class StartScreenPagerAdapter(val fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            NavHostFragment.create(R.navigation.current_hike_graph)
        }
        else {
            NavHostFragment.create(R.navigation.edit_routes_graph)
        }
    }
}