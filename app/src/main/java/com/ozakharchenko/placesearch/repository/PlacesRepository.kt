package com.ozakharchenko.placesearch.repository

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.api.APIClient
import com.ozakharchenko.placesearch.api.MetaResponse
import com.ozakharchenko.placesearch.api.RetrofitInstance
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object PlacesRepository {

    private val service: APIClient by lazy { RetrofitInstance.getRetrofitInstance().create(APIClient::class.java) }

    fun getPlacesFromAPI(
        category: SearchCategory, coordinates: String, query: String, radius: Int, limit: Int
    ): MutableLiveData<Resource<MetaResponse>> {
        val liveDataResponse = MutableLiveData<Resource<MetaResponse>>()
        liveDataResponse.value = Resource.loading(null)
        val call: Call<MetaResponse> = service.getNearByPlaces(category.category, coordinates, query, radius, limit)
        call.enqueue(object : Callback<MetaResponse> {

            override fun onFailure(call: Call<MetaResponse>, t: Throwable) {
                liveDataResponse.value = Resource.error(t.message, null)
            }

            override fun onResponse(call: Call<MetaResponse>, response: Response<MetaResponse>) {
                if (response.isSuccessful) {
                    liveDataResponse.value = Resource.success(response.body())
                } else {
                    liveDataResponse.value = Resource.error(null, null)
                }
            }
        })
        return liveDataResponse
    }
}