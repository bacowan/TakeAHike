package com.example.takeahike.ui.hike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.takeahike.R
import com.example.takeahike.presenter.CurrentHikePresenter
import com.example.takeahike.presenter.EditRoutePresenter
import com.example.takeahike.ui.edit.editor.ClickOverlay
import com.example.takeahike.ui.edit.editor.OnMarkerDragListener
import com.example.takeahike.uiEvents.currentHikeUIEvents.LoadRouteEvent
import com.example.takeahike.viewmodels.currentRoute.CurrentHikeViewModel
import com.example.takeahike.viewmodels.currentRoute.RecenterAction
import com.example.takeahike.viewmodels.editRoute.EditRouteViewModel
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class CurrentHike : Fragment() {
    private lateinit var map: MapView
    private lateinit var presenter : CurrentHikePresenter
    private val args: CurrentHikeArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = CurrentHikePresenter(resources.getString(R.string.map_quest_key))
        presenter.updateUIAction.subscribe { updateAction(it) }
        presenter.updateUI.subscribe { update(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.current_hike, container, false)

        map = view.findViewById(R.id.current_hike_map)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.update(LoadRouteEvent(args.routeId))
    }

    private fun update(viewmodel : CurrentHikeViewModel) {
        map.overlays.removeIf { it !is ClickOverlay }

        val roadOverlay = RoadManager.buildRoadOverlay(viewmodel.road)
        map.overlays.add(roadOverlay)

        map.invalidate()
    }

    private fun updateAction(action : RecenterAction) {
        map.controller.setCenter(GeoPoint(action.lat, action.lon))
        map.controller.setZoom(18.0)
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