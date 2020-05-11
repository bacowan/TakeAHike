package com.example.takeahike.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.takeahike.R
import com.example.takeahike.presenter.EditRoutePresenter
import com.example.takeahike.uiEvents.editRouteUIEvents.SetWaypointsUIEvent
import com.example.takeahike.viewmodels.EditRouteViewModel
import org.osmdroid.api.IGeoPoint
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*


class EditRoute : Fragment() {
    private lateinit var map: MapView
    private lateinit var presenter : EditRoutePresenter
    private lateinit var nodeIcon : Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = EditRoutePresenter(resources.getString(R.string.map_quest_key))
        presenter.updateUI.subscribe { update(it) }
        nodeIcon = resources.getDrawable(R.drawable.ic_location_on_black_24dp)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_route, container, false)

        map = view.findViewById(R.id.map)
        map.setMultiTouchControls(true)
        map.controller.setZoom(3.0)

        map.overlays.add(ClickOverlay { sendPresenterMarkerChange(it) })

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

    private fun sendPresenterMarkerChange(newPoint : GeoPoint? = null) {
        presenter.update(SetWaypointsUIEvent(
            ArrayList(map.overlays.filterIsInstance<Marker>().map { it.position }),
            newPoint
        ))
    }

    private fun update(viewmodel : EditRouteViewModel) {
        map.overlays.removeIf { it !is ClickOverlay }

        val roadOverlay = RoadManager.buildRoadOverlay(viewmodel.road)
        map.overlays.add(roadOverlay)

        viewmodel.waypoints.forEach {
            val marker = Marker(map)
            marker.position = it
            marker.icon = nodeIcon
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.infoWindow = null
            marker.isDraggable = true
            marker.setOnMarkerDragListener(OnMarkerDragListener { sendPresenterMarkerChange() })
            map.overlays.add(marker)
        }

        map.invalidate()
    }

}