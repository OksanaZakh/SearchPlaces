package com.ozakharchenko.placesearch.repository

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.api.APIService
import com.ozakharchenko.placesearch.api.MetaResponse
import com.ozakharchenko.placesearch.api.Venue
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.Resource

class FetchPlacesFromAPITask constructor(private val apiService: APIService,
                                         private val category: SearchCategory,
                                         private val coordinates: String,
                                         private val query: String,
                                         private val radius: Int,
                                         private val limit: Int) : Runnable {
    private val _liveData = MutableLiveData<Resource<List<PlaceItem>>>().apply { value = Resource.loading(null) }
    val liveData = _liveData

    override fun run() {
        val response = apiService.getNearByPlaces(category.category, coordinates, query, radius, limit).execute()
        if (response.isSuccessful) {
            _liveData.postValue(Resource.success(response.body()?.let {
                getPlaceItemsFromResponse(it)
            }))
        } else {
            val msg = response.errorBody()?.string()
            val errorMsg = if (msg.isNullOrEmpty()) {
                response.message()
            } else {
                msg
            }
            _liveData.postValue(Resource.error(errorMsg ?: "unknown error", null))
        }
    }

    private fun getPlaceItemsFromResponse(input: MetaResponse): List<PlaceItem> {
        val places = ArrayList<PlaceItem>()
        input.response.venues?.forEach {
            places.add(
                    PlaceItem(
                            it.id, it.name, it.location?.formattedAddress?.joinToString(), it.location?.lat, it.location?.lng,
                            it.location?.distance, it.categories?.get(0)?.name, parseUrl(it) ?: ""
                    )
            )
        }
        return places
    }

    private fun parseUrl(venue: Venue): String? {
        return venue.categories?.firstOrNull()?.let { it.icon?.prefix + 100 + it.icon?.suffix }
    }
}