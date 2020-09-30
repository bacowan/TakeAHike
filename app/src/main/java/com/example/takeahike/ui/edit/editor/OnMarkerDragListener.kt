package com.example.takeahike.ui.edit.editor

import org.osmdroid.views.overlay.Marker

class OnMarkerDragListener(private val onDragEnd : () -> Unit) : Marker.OnMarkerDragListener {
    override fun onMarkerDragEnd(marker: Marker?) {
        onDragEnd()
    }

    override fun onMarkerDragStart(marker: Marker?) {
    }

    override fun onMarkerDrag(marker: Marker?) {
    }
}