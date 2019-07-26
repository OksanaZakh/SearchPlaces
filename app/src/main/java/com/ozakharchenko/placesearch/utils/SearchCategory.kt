package com.ozakharchenko.placesearch.utils

enum class SearchCategory(var category: String){
    ENTERTAINMENT("4bf58dd8d48988d17c941735,52e81612bcbc57f1066b79e7,4bf58dd8d48988d18e941735,5032792091d4c4b30a586d5c,52e81612bcbc57f1066b79ef," +
            "4bf58dd8d48988d1f1931735,52e81612bcbc57f1066b79ea,4bf58dd8d48988d17f941735,4bf58dd8d48988d1e5931735,4bf58dd8d48988d193941735,4bf58dd8d48988d17b941735," +
            "4d4b7105d754a06376d81259,4d4b7105d754a06377d81259,4eb1daf44b900d56c88a4600"),
    ART("4bf58dd8d48988d1e2931735,56aa371be4b08b9a8d573532,4bf58dd8d48988d181941735,4bf58dd8d48988d1f2931735,507c8c4091d498d9fc8c67a9,4bf58dd8d48988d184941735," +
            "4bf58dd8d48988d182941735"),
    EDUCATIONAL("4d4b7105d754a06372d81259,4bf58dd8d48988d12f941735,4bf58dd8d48988d13b941735"),
    EVENTS("4d4b7105d754a06373d81259"),
    FOOD("4d4b7105d754a06374d81259"),
    SIGHTSEEING("56aa371be4b08b9a8d5734db,4deefb944765f83613cdba6e,5642206c498e4bfca532186c,5744ccdfe4b0c0459246b4d9,50328a8e91d4c4b30a586d6c"),
    GOVERNMENT("4bf58dd8d48988d126941735,4e52adeebd41615f56317744,56aa371be4b08b9a8d5734c5"),
    MEDICAL("4bf58dd8d48988d104941735"),
    SHOPPING("4d4b7105d754a06378d81259"),
    TRANSPORT("4d4b7105d754a06379d81259")
}

fun getCategory(str: String): SearchCategory {
    val category: SearchCategory by lazy {
        when (str) {
            "Entertainments" -> SearchCategory.ENTERTAINMENT
            "Art" -> SearchCategory.ART
            "Cafes and Restaurants" -> SearchCategory.FOOD
            "Sightseeing" -> SearchCategory.SIGHTSEEING
            "Educational" -> SearchCategory.EDUCATIONAL
            "Events" -> SearchCategory.EVENTS
            "Government buildings" -> SearchCategory.GOVERNMENT
            "Medical" -> SearchCategory.MEDICAL
            "Public transport" -> SearchCategory.TRANSPORT
            else -> SearchCategory.SHOPPING
        }
    }
    return category
}