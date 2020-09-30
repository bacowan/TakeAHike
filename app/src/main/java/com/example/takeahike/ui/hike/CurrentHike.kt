package com.example.takeahike.ui.hike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.takeahike.R
import com.google.android.material.snackbar.Snackbar
import org.osmdroid.views.MapView

class CurrentHike : Fragment() {
    private lateinit var map: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.current_hike, container, false)

        map = view.findViewById(R.id.current_hike_map)
        map.controller.setZoom(3.0)

        return view
    }
}