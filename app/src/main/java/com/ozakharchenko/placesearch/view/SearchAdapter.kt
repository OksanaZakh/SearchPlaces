package com.ozakharchenko.placesearch.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ozakharchenko.placesearch.R
import com.ozakharchenko.placesearch.model.PlaceItem
import org.jetbrains.annotations.NotNull

class SearchAdapter(private val places: List<PlaceItem>) : RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.venue_view_holder, parent, false))
    }

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(@NotNull holder: SearchViewHolder, position: Int) {
        holder.tvCategory.text = places[position].categoryName
        holder.tvName.text = places[position].name
        holder.tvDistance.text = places[position].distance.toString()
        /*TODO:
        -set click listener
        -icon
        -star
         */
    }
}