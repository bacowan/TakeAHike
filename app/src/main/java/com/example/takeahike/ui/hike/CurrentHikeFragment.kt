package com.example.takeahike.ui.hike

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.takeahike.R
import com.example.takeahike.backend.data.LatLng
import com.example.takeahike.ui.LocationUpdater
import com.example.takeahike.viewModels.CurrentHikeViewModel
import com.example.takeahike.ui.edit.editor.ClickOverlay
import com.example.takeahike.uiEvents.currentHikeUIEvents.RecenterEvent
import com.example.takeahike.uiEvents.currentHikeUIEvents.UpdatePositionEvent
import com.example.takeahike.uiEvents.currentHikeUIEvents.ViewLoadedEvent
import com.example.takeahike.viewData.currentRoute.CurrentHikeData
import com.example.takeahike.viewData.currentRoute.RecenterAction
import com.example.takeahike.viewModels.ActionPresenter
import com.google.android.gms.location.*
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class CurrentHikeFragment : Fragment() {
    private lateinit var map: MapView
    private lateinit var currentLocationIcon : Drawable
    private lateinit var locationUpdater: LocationUpdater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let { locationUpdater = LocationUpdater(
            LocationServices.getFusedLocationProviderClient(it),
            locationUpdateCallback) }

        val viewModel : CurrentHikeViewModel by viewModels()
        viewModel.data.observe(this, { update(it) })
        viewModel.action.observe(this, { it.handle { value -> updateAction(value) }})

        currentLocationIcon = resources.getDrawable(R.drawable.ic_person_pin_circle_black_48dp, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.current_hike, container, false)

        map = view.findViewById(R.id.current_hike_map)

        if (savedInstanceState != null) {
            with(savedInstanceState) {
                map.controller.setZoom(getDouble(currentZoomKey))
                map.controller.setCenter(GeoPoint(
                    getDouble(currentLatKey),
                    getDouble(currentLonKey)
                ))
            }
        }
        else {
            map.controller.setZoom(18.0)
        }

        val viewModel : CurrentHikeViewModel by viewModels()
        viewModel.update(ViewLoadedEvent())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewArButton: View = view.findViewById(R.id.view_ar_button)
        viewArButton.setOnClickListener {
            val action =
                CurrentHikeFragmentDirections.actionCurrentHikeToHikeAr()
            view.findNavController().navigate(action)
        }

        val recenterButton: View = view.findViewById(R.id.recenter_button)
        recenterButton.setOnClickListener {
            val viewModel : CurrentHikeViewModel by viewModels()
            viewModel.update(RecenterEvent())
        }
    }

    override fun onResume() {
        super.onResume()
        locationUpdater.startLocationUpdates()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        locationUpdater.endLocationUpdates()
        map.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putDouble(currentZoomKey, map.zoomLevelDouble)
        outState.putDouble(currentLatKey, map.mapCenter.latitude)
        outState.putDouble(currentLonKey, map.mapCenter.longitude)
        super.onSaveInstanceState(outState)
    }

    private val locationUpdateCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult?) {
            val viewModel : CurrentHikeViewModel by viewModels()
            if (location != null) {
                viewModel.update(
                    UpdatePositionEvent(
                        LatLng(
                            location.lastLocation.latitude,
                            location.lastLocation.longitude
                        )
                    )
                )
            }
        }
    }

    private fun update(viewmodel : CurrentHikeData) {
        map.overlays.removeIf { it !is ClickOverlay }

        val line = Polyline()
        line.setPoints(viewmodel.road)
        map.overlayManager.add(line)

        if (viewmodel.currentPosition != null) {
            val marker = Marker(map)
            marker.position = viewmodel.currentPosition
            marker.icon = currentLocationIcon
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            map.overlays.add(marker)
        }

        map.invalidate()
    }

    private fun updateAction(action : RecenterAction) {
        map.controller.setCenter(GeoPoint(action.lat, action.lon))
    }

    private companion object Constants {
        const val currentZoomKey = "currentZoom"
        const val currentLatKey = "currentLat"
        const val currentLonKey = "currentLon"
    }
}