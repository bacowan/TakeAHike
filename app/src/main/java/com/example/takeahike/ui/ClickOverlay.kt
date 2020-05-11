package com.example.takeahike.ui

import android.view.MotionEvent
import com.example.takeahike.presenter.EditRoutePresenter
import com.example.takeahike.uiEvents.editRouteUIEvents.SetWaypointsUIEvent
import org.osmdroid.api.IGeoPoint
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