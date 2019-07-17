package com.ozakharchenko.placesearch.usecases

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.AppExecutors
import com.ozakharchenko.placesearch.repository.PlaceItem
import com.ozakharchenko.placesearch.repository.Repo
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.Resource

class GetPlacesList(private val executors: AppExecutors,
                    private val repo: Repo,
                    private val category: SearchCategory,
                    private val coordinates: String,
                    private val query: String,
                    private val radius: Int,
                    private val limit: Int) : IUseCase<List<PlaceItem>> {

    override fun getPlacesDataWithFavourites(): MutableLiveData<Resource<List<PlaceItem>>> {
        //TODO: combine data from api and db
        return repo.getPlacesList(category, coordinates, query, radius, limit)
    }
}