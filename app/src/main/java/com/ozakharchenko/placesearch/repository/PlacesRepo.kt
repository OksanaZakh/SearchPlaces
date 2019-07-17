package com.ozakharchenko.placesearch.repository

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.AppExecutors
import com.ozakharchenko.placesearch.api.APIService
import com.ozakharchenko.placesearch.api.RetrofitInstance
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.Resource

class PlacesRepo : Repo {
    private val service = RetrofitInstance.getRetrofitInstance().create(APIService::class.java)

    override fun getPlaceDetails(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlacesList(category: SearchCategory, coordinates: String, query: String, radius: Int, limit: Int): MutableLiveData<Resource<List<PlaceItem>>> {
        val task = FetchPlacesFromAPITask(service, category, coordinates, query, radius, limit)
        AppExecutors.networkIO().execute(task)
        return task.liveData
    }
}