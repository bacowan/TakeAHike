package com.example.takeahike.ui.hike

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.takeahike.R
import com.example.takeahike.backend.data.CurrentHike
import com.example.takeahike.backend.utilities.CurrentHikeLogic
import com.google.gson.Gson

class NoCurrentHike : Fragment() {

    private val currentHikeLogic : CurrentHikeLogic = CurrentHikeLogic()
    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.no_current_hike, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val currentRoute = sharedPref?.getString(getString(R.string.current_hike_route_id), null)
        val hikeCompletion = sharedPref?.getFloat(getString(R.string.current_hike_progress), 0F)

        if (currentRoute != null && hikeCompletion != null) {
            val action =
                NoCurrentHikeDirections.actionNoCurrentHikeToCurrentHike(
                    CurrentHike(currentRoute, hikeCompletion.toDouble()))
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
            requetPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val requetPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
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
}