package com.ozakharchenko.placesearch.api

data class BaseResponse(var response: Response?) {
    constructor() : this(null)
}

data class Response(var venues: List<Venue>?) {
    constructor() : this(null)
}

data class Venue(var name: String, var location: Location?, var categories: List<Category>?) {
    constructor() : this("", null, null)
}

data class Location(var address: String, var lat: Double, var lng: Double, var distance: Int) {
    constructor() : this("", 0.0, 0.0, 0)
}

data class Category(var name: String, var icon: Icon?) {
    constructor() : this("", null)
}

data class Icon(var prefix: String, var suffix: String) {
    constructor() : this("", "")
}