package com.example.takeahike.ui

import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.takeahike.R
import com.example.takeahike.adapters.RouteListAdapter
import com.example.takeahike.backend.data.RouteSaveData
import com.example.takeahike.presenter.EditRoutesListPresenter
import com.example.takeahike.uiEvents.routeListUIEvents.ListReadyUIEvent
import com.example.takeahike.viewmodels.RouteListViewModel
import java.io.FileNotFoundException

class EditRoutesList : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val presenter : EditRoutesListPresenter = EditRoutesListPresenter()

    init {
        presenter.updateUI.subscribe { update(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_routes_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewManager = LinearLayoutManager(activity)

        val addButton: View = view.findViewById(R.id.fab)
        addButton.setOnClickListener {
            val action = EditRoutesListDirections
                .actionEditRoutesListToEditRoute()
            it.findNavController().navigate(action)
        }

        try {
            // TODO: Figure out how to cache so that this only gets called once per app load
            val bytes = context?.openFileInput("routes")?.readBytes()
            if (bytes != null) {
                presenter.update(ListReadyUIEvent(bytes))
            }
        }
        catch (e : FileNotFoundException) {
            // TODO: Error handling? This is a valid scenario though.
            //  Should probably log it somewhere, however.
        }

    }

    fun update(viewModel : RouteListViewModel) {
        viewAdapter = RouteListAdapter(viewModel.routes.map { it.name }.toTypedArray())

        view?.let {
            recyclerView = it.findViewById<RecyclerView>(R.id.recyclerView).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
    }
}