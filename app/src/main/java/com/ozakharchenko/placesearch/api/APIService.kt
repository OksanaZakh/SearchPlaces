package com.ozakharchenko.placesearch.api

import com.ozakharchenko.placesearch.utils.URL
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET(URL)
    fun getNearByPlaces(
        @Query("||") coordinates: String,
        @Query("radius") radius: Int,
        @Query("limit") limit: Int,
        @Query("categoryId") category: String
    )
}