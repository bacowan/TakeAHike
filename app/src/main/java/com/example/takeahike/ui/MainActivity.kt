package com.example.takeahike.ui

import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.addCallback
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.takeahike.R
import com.example.takeahike.backend.utilities.CurrentHikeLogic
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.realm.Realm
import org.osmdroid.config.Configuration


class MainActivity : FragmentActivity() {

    private val currentHikeLogic : CurrentHikeLogic = CurrentHikeLogic()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Realm.init(applicationContext)

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext))

        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.current_hike_nav -> {
                    val newFragment = supportFragmentManager.findFragmentByTag("hike")
                    if (newFragment != null) {
                        supportFragmentManager.beginTransaction().show(newFragment).commit()
                    }
                    else {
                        val newContainer = NavGraphHostFragment(R.navigation.current_hike_nav)
                        supportFragmentManager.beginTransaction().add(R.id.fragment_container, newContainer, "hike").commit()
                    }
                    val currentFragment = supportFragmentManager.findFragmentByTag("edit")
                    if (currentFragment != null) {
                        supportFragmentManager.beginTransaction().hide(currentFragment).commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.hike_editor_nav -> {
                    val newFragment = supportFragmentManager.findFragmentByTag("edit")
                    if (newFragment != null) {
                        supportFragmentManager.beginTransaction().show(newFragment).commit()
                    }
                    else {
                        val newContainer = NavGraphHostFragment(R.navigation.hike_editor_nav)
                        supportFragmentManager.beginTransaction().add(R.id.fragment_container, newContainer, "edit").commit()
                    }
                    val currentFragment = supportFragmentManager.findFragmentByTag("hike")
                    if (currentFragment != null) {
                        supportFragmentManager.beginTransaction().hide(currentFragment).commit()
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
        bottomNav.selectedItemId = R.id.current_hike_nav

        onBackPressedDispatcher.addCallback(this) {
            val editFragment = supportFragmentManager.findFragmentByTag("edit")
            val hikeFragment = supportFragmentManager.findFragmentByTag("hike")

            val fragment = if (editFragment?.isVisible == true) editFragment else hikeFragment
            if (fragment is NavGraphHostFragment) {
                if (!fragment.navController.popBackStack()) {
                    finish()
                }
            }
        }
    }
}
