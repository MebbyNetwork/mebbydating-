package com.example.data.interfaces

import com.example.domain.models.city.GeoDBModel
import retrofit2.Response
import retrofit2.http.GET

interface GeoDBApi {
    @GET("cities?limit=10")
    suspend fun getCities(): Response<GeoDBModel>
}