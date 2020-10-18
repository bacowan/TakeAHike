package com.example.takeahike.ui.hike

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.takeahike.R
import com.example.takeahike.backend.data.CurrentHike
import com.example.takeahike.backend.utilities.loadCurrentHike
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.ObjectInputStream

class NoCurrentHike : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.no_current_hike, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (currentHikeExists()) {
            val action =
                NoCurrentHikeDirections.actionNoCurrentHikeToCurrentHike()
            view.findNavController().navigate(action)
        }
        else {
            val button = view.findViewById<Button>(R.id.new_hike_button)
            button.setOnClickListener { newHikeClick(view) }
        }
    }

    private fun newHikeClick(view: View) {
        if (ContextCompat.checkSelfPermission(
                view.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            goToNewHike(view)
        }
        else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        view?.let {
            if (isGranted) {
                goToNewHike(it)
            } else {
                // TODO: handle selecting no for permission request
            }
        }
    }

    private fun goToNewHike(view: View) {
        val action =
            NoCurrentHikeDirections.actionSelectHikeToSelectRouteList()
        view.findNavController().navigate(action)
    }

    private fun currentHikeExists() : Boolean {
        val currentHikeFileName = getString(R.string.current_hike_file_name)
        return context?.let { loadCurrentHike(it, currentHikeFileName) } != null
    }
}