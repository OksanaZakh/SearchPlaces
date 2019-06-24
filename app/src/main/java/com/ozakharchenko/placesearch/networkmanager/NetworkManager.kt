package com.ozakharchenko.placesearch.networkmanager

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.api.APIService
import com.ozakharchenko.placesearch.api.BaseResponse
import com.ozakharchenko.placesearch.api.RetrofitInstance
import com.ozakharchenko.placesearch.utils.DownloadException
import com.ozakharchenko.placesearch.utils.SearchCategory
import com.ozakharchenko.placesearch.viewmodel.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object NetworkManager {
    private val service: APIService by lazy { RetrofitInstance.getRetrofitInstance().create(APIService::class.java) }
    var limit = 20
    var radius = 1000

    //TODO if no internet connection option
    // TODO diff coordinates

    fun getPlaces(coordinates: String, category: SearchCategory): MutableLiveData<Resource<BaseResponse>> {
        val liveDataResponse = MutableLiveData<Resource<BaseResponse>>()
        val call: Call<BaseResponse> = service.getNearByPlaces(coordinates, radius, limit, category.category)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                val exception = DownloadException(t)
                liveDataResponse.value = Resource.error(exception)
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                liveDataResponse.value = Resource.success(response.body())
            }
        })
        return liveDataResponse
    }
}