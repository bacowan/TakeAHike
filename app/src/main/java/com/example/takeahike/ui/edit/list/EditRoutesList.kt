package com.example.takeahike.ui.edit.list

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
import com.example.takeahike.ui.adapters.RouteListAdapter
import com.example.takeahike.viewModels.EditRoutesListViewModel
import com.example.takeahike.ui.adapters.AdapterItemClickListener
import com.example.takeahike.uiEvents.routeListUIEvents.ListReadyUIEvent
import com.example.takeahike.viewData.editRouteList.EditRouteListData

class EditRoutesList : Fragment() {
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: EditRoutesListViewModel by viewModels()
        viewModel.data.observe(this, Observer { update(it) })
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
            val action =
                EditRoutesListDirections.actionEditRoutesListToEditRoute()
            it.findNavController().navigate(action)
        }

        val viewModel: EditRoutesListViewModel by viewModels()
        viewModel.update(ListReadyUIEvent())
    }

    private fun update(data : EditRouteListData) {
        val viewAdapter = RouteListAdapter(
            data.routes.map { it.name }.toTypedArray(),
            object:AdapterItemClickListener {
                override fun onItemClick(view: View, position: Int) {
                    val id = data.routes[position].id
                    val action =
                        EditRoutesListDirections.actionEditRoutesListToEditRoute(id)
                    view.findNavController().navigate(action)
                }
            }
        )

        view?.let {
            it.findViewById<RecyclerView>(R.id.edit_list_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = viewAdapter
            }
        }
    }
}