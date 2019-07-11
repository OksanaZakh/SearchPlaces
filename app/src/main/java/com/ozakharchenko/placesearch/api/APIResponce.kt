package com.ozakharchenko.placesearch.api

data class MetaResponse(val response: PlacesResponse)

data class PlacesResponse(var venues: List<Venue>? = null)

data class DetailedResponse(var venue: Venue? = null)

data class Venue(val id: String, var name: String = "", var location: Location? = null, var categories: List<Category>? = null, var url: String? = null, var rating: Float? = null, var parant: Venue? = null)

data class Location(var formattedAddress: Array<String?> = arrayOfNulls<String?>(3), var lat: Double = 0.0, var lng: Double = 0.0, var distance: Int = 0)

data class Category(var name: String = "", var icon: Icon? = null)

data class Icon(var prefix: String = "", var suffix: String = "")