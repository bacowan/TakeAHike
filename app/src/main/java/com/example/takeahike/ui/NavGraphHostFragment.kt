package com.example.takeahike.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.takeahike.R

class NavGraphHostFragment : NavHostFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("graphResId")?.let {
            navController.graph = navController.navInflater.inflate(it)
        }
    }
}

fun createNavGraphHostFragment(graphResId: Int): NavGraphHostFragment {
    val args = Bundle()
    args.putInt("graphResId", graphResId)
    val frag = NavGraphHostFragment()
    frag.arguments = args
    return frag
}