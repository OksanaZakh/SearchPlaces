package com.ozakharchenko.placesearch.repository

data class PlaceItem(
        var id: String,
        var name: String,
        var address: String?,
        var lat: Double?,
        var lng: Double?,
        var distance: Int?,
        var categoryName: String?,
        var imageUrl: String,
        var isFavourite: Boolean = false,
        var url: String? = null,
        var rating: Float? = null,
        var parent: PlaceItem? = null
)