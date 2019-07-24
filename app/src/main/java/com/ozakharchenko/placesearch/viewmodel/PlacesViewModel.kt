package com.ozakharchenko.placesearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ozakharchenko.placesearch.AppExecutors
import com.ozakharchenko.placesearch.repository.PlaceItem
import com.ozakharchenko.placesearch.repository.PlacesRepo
import com.ozakharchenko.placesearch.usecases.GetPlacesList
import com.ozakharchenko.placesearch.utils.SearchCategory

class PlacesViewModel : ViewModel() {

    val TAG = "View Model"

    fun getPlaces(
            category: SearchCategory,
            coordinates: String,
            query: String,
            radius: Int = 10_000,
            limit: Int = 50
    ): LiveData<Resource<List<PlaceItem>>> {
        Log.e(TAG, "Location ccords $coordinates")
        val useCase = GetPlacesList(AppExecutors, PlacesRepo(), category, coordinates, query, radius, limit)
        return useCase.getPlacesDataWithFavourites()
    }
}