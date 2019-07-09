package com.ozakharchenko.placesearch.api

data class BaseResponse(var response: Response? = null)

data class Response(var venues: List<Venue>? = null)

data class Venue(var name: String = "", var location: Location? = null, var categories: List<Category>? = null)

data class Location(var formattedAddress: Array<String?> = arrayOfNulls<String?>(3), var lat: Double = 0.0, var lng: Double = 0.0, var distance: Int = 0)

data class Category(var name: String = "", var icon: Icon? = null)

data class Icon(var prefix: String = "", var suffix: String = "")