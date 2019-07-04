package com.ozakharchenko.placesearch.api

import com.ozakharchenko.placesearch.utils.API_CLIENT_ID
import com.ozakharchenko.placesearch.utils.API_CLIENT_SECRET
import com.ozakharchenko.placesearch.utils.API_CLIENT_VERSION
import com.ozakharchenko.placesearch.utils.URL
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIClient {
    @GET(URL)
    fun getNearByPlaces(
        @Query("categoryId") category: String,
        @Query("ll") coordinates: String,
        @Query("query") query: String,
        @Query("radius") radius: Int,
        @Query("limit") limit: Int,
        @Query("client_id") clientId: String = API_CLIENT_ID,
        @Query("client_secret") clientSecret: String = API_CLIENT_SECRET,
        @Query("v") version: String = API_CLIENT_VERSION
    ): Call<BaseResponse>
}