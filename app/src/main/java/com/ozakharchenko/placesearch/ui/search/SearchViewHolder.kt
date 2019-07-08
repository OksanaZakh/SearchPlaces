package com.ozakharchenko.placesearch.ui.search

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.venue_view_holder.view.*

class SearchViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tvName = view.tvName
    val tvCategory = view.tvCategory
    val tvDistance = view.tvDistance
    val tvIcon = view.ivIcon
    val ivStar = view.ivStar
}