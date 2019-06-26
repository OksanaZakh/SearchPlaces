package com.ozakharchenko.placesearch.model

data class PlaceItem(
    var name: String,
    var address: String?,
    var lat: Double?,
    var lng: Double?,
    var distance: Int?,
    var categoryName: String?,
    var imageUrl: String,
    var isFavourite: Boolean = false
)