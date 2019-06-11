package com.ozakharchenko.placesearch.networkmanager

import androidx.lifecycle.MutableLiveData
import com.ozakharchenko.placesearch.api.APIService
import com.ozakharchenko.placesearch.api.BaseResponse
import com.ozakharchenko.placesearch.api.RetrofitInstance
import com.ozakharchenko.placesearch.utils.SearchCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NetworkManager {
    private val service: APIService by lazy { RetrofitInstance.getRetrofitInstance().create(APIService::class.java) }
    var limit = 20
    var radius = 1000


    //TODO if no internet connection option
    // TODO diff coordinates

    fun getVenues(coordinates: String, category: SearchCategory): MutableLiveData<BaseResponse> {
        var liveDataResponse: MutableLiveData<BaseResponse> = MutableLiveData()
        val call: Call<BaseResponse> = service.getNearByPlaces(coordinates, radius, limit, category.category)
        call.enqueue(object : Callback<BaseResponse> {
            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                liveDataResponse.value = null
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    liveDataResponse.value = response.body()
                } else {
                    liveDataResponse.value = null
                }
            }
        })
        return liveDataResponse
    }
}