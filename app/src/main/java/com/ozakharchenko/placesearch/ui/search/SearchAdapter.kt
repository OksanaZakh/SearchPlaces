package com.ozakharchenko.placesearch.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ozakharchenko.placesearch.repository.PlaceItem
import com.ozakharchenko.placesearch.ui.listeners.OnAddToFavouriteListener
import com.ozakharchenko.placesearch.ui.listeners.OnItemClickListener
import org.jetbrains.annotations.NotNull


class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {
    private var places: List<PlaceItem> = ArrayList()
    private lateinit var context: Context
    private lateinit var itemListener: OnItemClickListener
    private lateinit var addToFavouriteListener: OnAddToFavouriteListener

    fun setPlaces(places: List<PlaceItem>) {
        this.places = places
    }

    fun setItemClickListener(itemListener: OnItemClickListener) {
        this.itemListener = itemListener
    }

    fun setAddToFavouriteListener(addToFavouriteListener: OnAddToFavouriteListener) {
        this.addToFavouriteListener = addToFavouriteListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        context = parent.context
        return SearchViewHolder(
                LayoutInflater.from(context).inflate(
                        com.ozakharchenko.placesearch.R.layout.venue_view_holder,
                        parent,
                        false
                )
        )
    }

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(@NotNull holder: SearchViewHolder, position: Int) {
        holder.apply {
            tvCategory.text = places[position].categoryName
            tvName.text = places[position].name
            tvAddress.text = places[position].address ?: ""
            tvDistance.text = places[position].distance.toString()
            Glide.with(context).load(places[position].imageUrl).into(tvIcon)
            itemView.setOnClickListener {
                itemListener.onItemClick(position)
            }
            ivStar.setOnClickListener {
                addToFavouriteListener.onAddToFavourite(position)
            }

        }
    }
}
