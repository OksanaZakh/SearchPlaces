package com.ozakharchenko.placesearch.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ozakharchenko.placesearch.R
import com.ozakharchenko.placesearch.model.PlaceItem
import org.jetbrains.annotations.NotNull

class SearchAdapter() : RecyclerView.Adapter<SearchViewHolder>() {
    var places: List<PlaceItem> = ArrayList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        context = parent.context
        return SearchViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.venue_view_holder,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(@NotNull holder: SearchViewHolder, position: Int) {
        holder.tvCategory.text = places[position].categoryName
        holder.tvName.text = places[position].name
        holder.tvAddress.text = places[position].address?:""
        holder.tvDistance.text = places[position].distance.toString()
        Glide.with(context).load(places[position].imageUrl).into(holder.tvIcon)
                /*
        -set click listener
        -star
         */
    }
}