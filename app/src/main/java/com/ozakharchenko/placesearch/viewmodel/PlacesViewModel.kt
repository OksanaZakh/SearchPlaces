package com.ozakharchenko.placesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ozakharchenko.placesearch.AppExecutors
import com.ozakharchenko.placesearch.repository.PlaceItem
import com.ozakharchenko.placesearch.repository.PlacesRepo
import com.ozakharchenko.placesearch.usecases.GetPlacesList
import com.ozakharchenko.placesearch.utils.SearchCategory

class PlacesViewModel : ViewModel() {


    private var category: SearchCategory? = null
    private var coordinates: String? = null
    private var query: String? = null
    private var places: MutableLiveData<Resource<List<PlaceItem>>> = MutableLiveData()

    fun getPlaces(
        category: SearchCategory,
        coordinates: String,
        query: String,
        radius: Int = 10_000,
        limit: Int = 50
    ): LiveData<Resource<List<PlaceItem>>> {
        if (this.category != category || this.coordinates != coordinates || this.query != query) {
            this.category = category
            this.coordinates = coordinates
            this.query = query
            places = loadPlaces(category, coordinates, query, radius, limit)
        }
        return places
    }

    private fun loadPlaces(
        category: SearchCategory,
        coordinates: String,
        query: String,
        radius: Int,
        limit: Int
    ): MutableLiveData<Resource<List<PlaceItem>>> {
        return GetPlacesList(
            AppExecutors,
            PlacesRepo(),
            category,
            coordinates,
            query,
            radius,
            limit
        ).getPlacesDataWithFavourites()
    }
}