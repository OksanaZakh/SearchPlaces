package com.ozakharchenko.placesearch.repository

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.api.APIClient
import com.ozakharchenko.placesearch.api.BaseResponse
import com.ozakharchenko.placesearch.api.RetrofitInstance
import com.ozakharchenko.placesearch.utils.DownloadException
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object PlacesRepository {

    private val service: APIClient by lazy { RetrofitInstance.getRetrofitInstance().create(APIClient::class.java) }

    fun getPlacesFromAPI(
        category: SearchCategory, coordinates: String, query: String, radius: Int, limit: Int
    ): MutableLiveData<Resource<BaseResponse>> {
        val liveDataResponse = MutableLiveData<Resource<BaseResponse>>()
        liveDataResponse.value = Resource.loading(null)
        val call: Call<BaseResponse> = service.getNearByPlaces(category.category, coordinates, query, radius, limit)
        call.enqueue(object : Callback<BaseResponse> {

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                liveDataResponse.value = Resource.error(DownloadException(t))
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    liveDataResponse.value = Resource.success(response.body())
                } else {
                    liveDataResponse.value = Resource.error(DownloadException(null))
                }
            }
        })
        return liveDataResponse
    }
}