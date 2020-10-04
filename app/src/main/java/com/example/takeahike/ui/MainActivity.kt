package com.example.takeahike.ui

import android.os.Bundle
import android.os.StrictMode
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.example.takeahike.R
import com.example.takeahike.backend.utilities.CurrentHikeLogic
import com.example.takeahike.viewModels.MainActivityViewModel
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
        // TODO: change
        Configuration.getInstance().userAgentValue = "changeme"
        //Configuration.getInstance().load(applicationContext, PreferenceManager.getDefaultSharedPreferences(applicationContext));

        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnNavigationItemSelectedListener {
            val viewModel : MainActivityViewModel by viewModels()
            when (it.itemId) {
                R.id.current_hike_nav -> {
                    val newFragment = supportFragmentManager.findFragmentByTag("hike")
                    if (newFragment != null) {
                        supportFragmentManager.beginTransaction().show(newFragment).commit()
                    }
                    else {
                        val newContainer = createNavGraphHostFragment(R.navigation.current_hike_nav)
                        supportFragmentManager.beginTransaction().add(R.id.fragment_container, newContainer, "hike").commit()
                    }
                    val currentFragment = supportFragmentManager.findFragmentByTag("edit")
                    if (currentFragment != null) {
                        supportFragmentManager.beginTransaction().hide(currentFragment).commit()
                    }
                    viewModel.currentTab = R.id.current_hike_nav
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.hike_editor_nav -> {
                    val newFragment = supportFragmentManager.findFragmentByTag("edit")
                    if (newFragment != null) {
                        supportFragmentManager.beginTransaction().show(newFragment).commit()
                    }
                    else {
                        val newContainer = createNavGraphHostFragment(R.navigation.hike_editor_nav)
                        supportFragmentManager.beginTransaction().add(R.id.fragment_container, newContainer, "edit").commit()
                    }
                    val currentFragment = supportFragmentManager.findFragmentByTag("hike")
                    if (currentFragment != null) {
                        supportFragmentManager.beginTransaction().hide(currentFragment).commit()
                    }
                    viewModel.currentTab = R.id.hike_editor_nav
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }

        val viewModel : MainActivityViewModel by viewModels()
        val currentTab = viewModel.currentTab
        if (currentTab != null) {
            bottomNav.selectedItemId = currentTab
        }
        else {
            bottomNav.selectedItemId = R.id.current_hike_nav
            viewModel.currentTab = bottomNav.selectedItemId
        }

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
