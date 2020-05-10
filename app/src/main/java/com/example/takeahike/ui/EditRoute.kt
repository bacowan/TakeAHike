package com.example.takeahike.ui

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.takeahike.R
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.OverlayItem


class EditRoute : Fragment() {
    private lateinit var map: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_route, container, false)

        Configuration.getInstance().load(activity?.applicationContext, PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext))
        map = view.findViewById(R.id.map)
        map.setMultiTouchControls(true)
        map.controller.setZoom(3.0)

        map.overlays.add(MapClickOverlay())

        return view
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }


}