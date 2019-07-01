package com.ozakharchenko.placesearch.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ozakharchenko.placesearch.model.PlaceItem
import org.jetbrains.annotations.NotNull

class SearchAdapter(val places: ArrayList<PlaceItem>) : RecyclerView.Adapter<SearchViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(@NotNull holder: SearchViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}