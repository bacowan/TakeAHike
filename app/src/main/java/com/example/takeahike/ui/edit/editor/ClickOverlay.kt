package com.example.takeahike.ui.edit.editor

import android.view.MotionEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay

class ClickOverlay(private val onClick: (GeoPoint) -> Unit) : Overlay() {
    override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
        if (e != null && mapView != null) {
            val point = mapView.projection.fromPixels(e.x.toInt(), e.y.toInt()) as GeoPoint
            onClick(point)
        }
        return super.onSingleTapConfirmed(e, mapView)
    }
}