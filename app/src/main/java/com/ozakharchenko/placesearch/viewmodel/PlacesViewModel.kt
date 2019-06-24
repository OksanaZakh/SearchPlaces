package com.ozakharchenko.placesearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ozakharchenko.placesearch.api.BaseResponse
import com.ozakharchenko.placesearch.model.PlaceItem
import com.ozakharchenko.placesearch.networkmanager.NetworkManager
import com.ozakharchenko.placesearch.utils.SearchCategory

class PlacesViewModel : ViewModel() {

    var coordinates: String = "50.4494,30.5149"
    var category = SearchCategory.SHOPPING

    private val places: MutableLiveData<Resource<List<PlaceItem>>> by lazy {
        MutableLiveData<Resource<List<PlaceItem>>>().also {
            loadPlaces(coordinates, category)
        }
    }

    private fun loadPlaces(coordinates: String, category: SearchCategory) {
//        return Transformations.switchMap(
//            NetworkManager.getPlaces(coordinates, category), getTransformedValues
    }

    private fun getTransformedValues(networkItems: Resource<BaseResponse>): Resource<List<PlaceItem>> {
        when (networkItems.status) {
            Resource.Status.LOADING -> {
                return Resource.loading(null)
            }
            Resource.Status.ERROR -> {
                return Resource.error(networkItems.exception)
            }
            Resource.Status.SUCCESS -> {
                var places = ArrayList<PlaceItem>()
                networkItems?.data?.response?.venues?.forEach {
                    places.add(
                        PlaceItem(
                            it.name, it.location?.address, it.location?.lat, it.location?.lng,
                            it.location?.distance, it.categories?.get(0)?.name, "", false
                        )
                    )
                }

            }
        }
        return Resource.loading(null)
    }

}