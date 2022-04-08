package com.mobile.travelapp.repo.service

import com.mobile.travelapp.repo.model.PlacesWrapper
import retrofit2.http.GET

interface TravelApi {

    @GET("/tables/place")
    suspend fun fetchPopularPlaces(): PlacesWrapper
}
