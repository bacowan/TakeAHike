package com.example.takeahike.ui.hike

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takeahike.R
import com.example.takeahike.backend.data.CurrentHike
import com.example.takeahike.viewModels.SelectRouteViewModel
import com.example.takeahike.ui.adapters.AdapterItemClickListener
import com.example.takeahike.ui.adapters.RouteListAdapter
import com.example.takeahike.uiEvents.routeListUIEvents.ListReadyUIEvent
import com.example.takeahike.viewData.selectRoute.SelectRouteData
import com.example.takeahike.viewModels.EditRoutesListViewModel

class SelectRoute : Fragment() {
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: SelectRouteViewModel by viewModels()
        viewModel.data.observe(this, Observer { update(it) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.select_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = LinearLayoutManager(activity)
    }

    private fun update(data: SelectRouteData) {
        val viewAdapter = RouteListAdapter(
            data.routes.map { it.name }.toTypedArray(),
            object: AdapterItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val currentHike = CurrentHike(
                        data.routes[position].id,
                        0.0
                    )
                    val action =
                        SelectRouteDirections.actionSelectRouteListToCurrentHike(currentHike)
                    view.findNavController().navigate(action)
                }
            }
        )

        view?.let {
            it.findViewById<RecyclerView>(R.id.select_route_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
    }
}