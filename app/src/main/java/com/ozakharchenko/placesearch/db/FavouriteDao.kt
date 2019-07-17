package com.ozakharchenko.placesearch.db

interface FavouriteDao {

    fun getFavouritesList(): List<FavouritePlace>

    fun deleteFromFavourite(place: FavouritePlace)

    fun addToFavourite(place: FavouritePlace)

}