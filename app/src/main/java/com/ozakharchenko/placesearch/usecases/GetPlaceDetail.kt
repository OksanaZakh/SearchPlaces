package com.ozakharchenko.placesearch.usecases

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.AppExecutors
import com.ozakharchenko.placesearch.repository.PlaceItem
import com.ozakharchenko.placesearch.repository.PlacesRepo
import com.ozakharchenko.placesearch.viewmodel.Resource


class GetPlaceDetail(executors: AppExecutors, detailPlaceRepo: PlacesRepo) : IUseCase<PlaceItem> {
    override fun getPlacesDataWithFavourites(): MutableLiveData<Resource<PlaceItem>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}