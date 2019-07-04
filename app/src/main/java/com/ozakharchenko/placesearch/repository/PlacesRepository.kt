package com.ozakharchenko.placesearch.repository

import android.util.Log
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
    const val TAG = "Places Repo"

    private val service: APIClient by lazy { RetrofitInstance.getRetrofitInstance().create(APIClient::class.java) }

    fun getPlacesFromAPI(
        category: SearchCategory, coordinates: String, query: String, radius: Int, limit: Int
    ): MutableLiveData<Resource<BaseResponse>> {
        Log.e(TAG, "Response is .....")
        val liveDataResponse = MutableLiveData<Resource<BaseResponse>>()
        liveDataResponse.value = Resource.loading(null)
        val call: Call<BaseResponse> = service.getNearByPlaces(category.category, coordinates, query, radius, limit)
        call.enqueue(object : Callback<BaseResponse> {

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                liveDataResponse.value = Resource.error(DownloadException(t))
                Log.e(TAG, "Response is Fail")
            }

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                Log.e(TAG, "Response is ${response.code()}")
                if (response.isSuccessful) {
                    Log.e(TAG, "Response is Success")
                    liveDataResponse.value = Resource.success(response.body())
                    Log.e(TAG, "Input liveData status 1: ${liveDataResponse.value?.status} ${liveDataResponse.value?.data.toString()}")
                } else {
                    liveDataResponse.value = Resource.error(DownloadException(null))
                    Log.e(TAG, "Response is Error in response")
                }
            }
        })
        Log.e(TAG, "Input liveData status 2: ${liveDataResponse.value?.status}")
        return liveDataResponse
    }
}