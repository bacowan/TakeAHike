package com.example.takeahike.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.takeahike.R
import com.example.takeahike.presenter.EditRoutePresenter
import com.example.takeahike.presenter.Presenter
import com.example.takeahike.uiEvents.editRouteUIEvents.AddWaypointEvent
import com.example.takeahike.viewmodels.EditRouteViewModel
import org.osmdroid.bonuspack.location.NominatimPOIProvider
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
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

        val overlay = object : Overlay() {
            override fun onSingleTapConfirmed(e: MotionEvent?, mapView: MapView?): Boolean {
                if (e != null && mapView != null) {
                    val point = mapView.projection.fromPixels(e.x.toInt(), e.y.toInt())
                    presenter.update(AddWaypointEvent(
                        point.latitude,
                        point.longitude
                    ))
                }
                return super.onSingleTapConfirmed(e, mapView)
            }
        }

        map.overlays.add(overlay)

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

    private fun update(viewmodel : EditRouteViewModel) {
        map.overlays.removeIf { it is Polyline }
        val roadOverlay = RoadManager.buildRoadOverlay(viewmodel.road)
        map.overlays.add(roadOverlay)
        map.invalidate()
    }

}