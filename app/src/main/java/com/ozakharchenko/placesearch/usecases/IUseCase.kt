package com.ozakharchenko.placesearch.usecases

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.viewmodel.Resource

interface IUseCase<T> {
    fun getPlacesDataWithFavourites(): MutableLiveData<Resource<T>>
}