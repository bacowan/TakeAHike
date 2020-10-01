package com.example.takeahike.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.takeahike.R

class RouteListAdapter(private val dataset: Array<String>, private val onItemClickListener: AdapterItemClickListener)
    : RecyclerView.Adapter<RouteListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteListItem {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.route_list_item, parent, false)
        return RouteListItem(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: RouteListItem, position: Int) {
        holder.textView.text = dataset[position]
    }

    override fun getItemCount(): Int = dataset.size
}