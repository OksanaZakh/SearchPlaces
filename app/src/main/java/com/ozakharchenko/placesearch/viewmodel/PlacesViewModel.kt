package com.ozakharchenko.placesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ozakharchenko.placesearch.api.BaseResponse
import com.ozakharchenko.placesearch.api.Venue
import com.ozakharchenko.placesearch.model.PlaceItem
import com.ozakharchenko.placesearch.repository.PlacesRepository
import com.ozakharchenko.placesearch.utils.COORDINATE_KYIV_CENTER
import com.ozakharchenko.placesearch.utils.SearchCategory

class PlacesViewModel : ViewModel() {

    fun getPlaces(
            category: SearchCategory,
            coordinates: String = COORDINATE_KYIV_CENTER,
            query: String = "",
            radius: Int = 10_000,
            limit: Int = 50
    ): LiveData<Resource<List<PlaceItem>>> {
        val inputLiveData: LiveData<Resource<BaseResponse>> =
                PlacesRepository.getPlacesFromAPI(category, coordinates, query, radius, limit)
        return Transformations.map(inputLiveData) { input -> getTransformedValues(input) }
    }

    private fun getTransformedValues(inputLiveData: Resource<BaseResponse>): Resource<List<PlaceItem>> {
        when (inputLiveData.status) {
            Resource.Status.LOADING -> {
                return Resource.loading(null)
            }
            Resource.Status.ERROR -> {
                return Resource.error(inputLiveData.exception)
            }
            Resource.Status.SUCCESS -> {
                val places = ArrayList<PlaceItem>()
                inputLiveData.data?.response?.venues?.forEach {
                    places.add(
                            PlaceItem(
                                    it.name, it.location?.formattedAddress?.joinToString(), it.location?.lat, it.location?.lng,
                                    it.location?.distance, it.categories?.get(0)?.name, parseUrl(it)?:""
                            )
                    )
                }
                return Resource.success(places)
            }
        }
    }

    private fun parseUrl(venue: Venue): String?{
        return venue.categories?.firstOrNull()?.let { it.icon?.prefix + 100 + it.icon?.suffix }
    }
}