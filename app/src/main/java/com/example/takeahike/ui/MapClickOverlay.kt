package com.example.takeahike.ui

import android.view.MotionEvent
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class MapClickOverlay : Overlay() {
    private val _coordinates: MutableList<Pair<Double, Double>> = mutableListOf()
    val coordinates: List<Pair<Double, Double>>
        get() = _coordinates.toList()

    override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
        if (e != null && mapView != null) {
            val point = mapView.projection.fromPixels(e.x.toInt(), e.y.toInt())
            _coordinates.add(Pair(point.latitude, point.longitude))
        }

        return super.onSingleTapConfirmed(e, mapView)
    }
}