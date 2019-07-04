package com.ozakharchenko.placesearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ozakharchenko.placesearch.api.BaseResponse
import com.ozakharchenko.placesearch.model.PlaceItem
import com.ozakharchenko.placesearch.repository.PlacesRepository
import com.ozakharchenko.placesearch.utils.SearchCategory

class PlacesViewModel : ViewModel() {

    val TAG = "Places View Model"

    var coordinates = "50.4494,30.5149" // Kyiv center
    var category = SearchCategory.ART
    var query = ""
    var limit = 20
        set(value) {
            if (value in 0..50) field = value
        }

    var radius = 10000
        set(value) {
            if (value in 0..10_000) field = value
        }

    private var places: LiveData<Resource<List<PlaceItem>>> = loadPlaces()

    fun getPlaces(): LiveData<Resource<List<PlaceItem>>> {
        Log.e(TAG, "The coords are$coordinates, category is ${category.name}")
        return places
    }

    private fun loadPlaces(): LiveData<Resource<List<PlaceItem>>> {
        val inputLiveData: MutableLiveData<Resource<BaseResponse>> =
            PlacesRepository.getPlacesFromAPI(category, coordinates, query, radius, limit)
        Log.e(TAG, "Category ${category.name}, coords $coordinates, query $query, radius $radius, limit $limit")
        Log.e(TAG, "Input liveData status: ${inputLiveData.value?.status} ${inputLiveData.value?.data.toString()}")
        return Transformations.map(inputLiveData) { input -> getTransformedValues(input) }
    }

    private fun getTransformedValues(inputLiveData: Resource<BaseResponse>): Resource<List<PlaceItem>> {
        Log.e(TAG, "Status of input ${inputLiveData.status}")
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