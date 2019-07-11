package com.ozakharchenko.placesearch.api

import com.ozakharchenko.placesearch.utils.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIClient {
    @GET(SEARCH_FOR_VENUES_URL)
    fun getNearByPlaces(@Query("categoryId") category: String,
                        @Query("ll") coordinates: String,
                        @Query("query") query: String,
                        @Query("radius") radius: Int,
                        @Query("limit") limit: Int,
                        @Query("client_id") clientId: String = API_CLIENT_ID,
                        @Query("client_secret") clientSecret: String = API_CLIENT_SECRET,
                        @Query("v") version: String = API_CLIENT_VERSION
    ): Call<MetaResponse>

    @GET(DETAILED_PLACE_URL)
    fun getDetailsOfVenue(@Path("VENUE_ID") id: String,
                          @Query("client_id") clientId: String = API_CLIENT_ID,
                          @Query("client_secret") clientSecret: String = API_CLIENT_SECRET,
                          @Query("v") version: String = API_CLIENT_VERSION): Call<MetaResponse>
}