package com.example.takeahike.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.takeahike.R
import com.example.takeahike.backend.utilities.CurrentHikeLogic
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
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

        val navController = findNavController(R.id.nav_host_fragment)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setupWithNavController(navController)
        bottomNav.setOnNavigationItemSelectedListener {
            it.onNavDestinationSelected(navController) || super.onOptionsItemSelected(it)
        }
    }
}
