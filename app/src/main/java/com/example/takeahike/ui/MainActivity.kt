package com.example.takeahike.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.takeahike.R
import com.example.takeahike.adapters.StartScreenPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : FragmentActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        viewPager.adapter = StartScreenPagerAdapter(this)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) {
                "Current Hike"
            }
            else {
                "Edit Routes"
            }
        }.attach()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            viewPager.
        }
        else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}
