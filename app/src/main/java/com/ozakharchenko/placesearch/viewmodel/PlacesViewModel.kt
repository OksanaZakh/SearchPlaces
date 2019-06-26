package com.ozakharchenko.placesearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ozakharchenko.placesearch.api.BaseResponse
import com.ozakharchenko.placesearch.model.PlaceItem
import com.ozakharchenko.placesearch.repository.PlacesRepository
import com.ozakharchenko.placesearch.utils.SearchCategory

class PlacesViewModel : ViewModel() {

    private val coordinates = "50.4494,30.5149" // Kyiv center
    private val category = SearchCategory.SHOPPING
    var limit = 20
        set(value) {
            if (value in 0..50) field = value
        }

    var radius = 1000
        set(value) {
            if (value in 0..10_000) field = value
        }

    private val places: MutableLiveData<Resource<List<PlaceItem>>> by lazy {
        MutableLiveData<Resource<List<PlaceItem>>>().also {
            loadPlaces(category, coordinates)
        }
    }

    fun getPlaces(
        coordinates: String = this.coordinates,
        category: SearchCategory = this.category
    ): LiveData<Resource<List<PlaceItem>>> {
        return places
    }

    private fun loadPlaces(
        category: SearchCategory,
        coordinates: String,
        query: String = "",
        limit: Int = this.limit,
        radius: Int = this.radius
    ): LiveData<Resource<List<PlaceItem>>> {
        val inputLiveData: MutableLiveData<Resource<BaseResponse>> =
            PlacesRepository.getPlaces(category, coordinates, query, radius, limit)
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
                            it.name, it.location?.address, it.location?.lat, it.location?.lng,
                            it.location?.distance, it.categories?.get(0)?.name, ""
                        )
                    )
                }

            }
        }
        return Resource.loading(null)
    }

}