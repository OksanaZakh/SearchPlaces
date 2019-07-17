package com.ozakharchenko.placesearch.repository

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.Resource

interface Repo {

    fun getPlaceDetails(id: String)

    fun getPlacesList(category: SearchCategory, coordinates: String, query: String, radius: Int, limit: Int): MutableLiveData<Resource<List<PlaceItem>>>
}