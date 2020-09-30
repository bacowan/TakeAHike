package com.example.takeahike.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.takeahike.R

class NavGraphHostFragment(private val graphResId: Int) : NavHostFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController.graph = navController.navInflater.inflate(graphResId)
    }
}