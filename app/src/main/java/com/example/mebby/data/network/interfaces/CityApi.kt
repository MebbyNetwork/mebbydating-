package com.example.mebby.data.network.interfaces

import com.example.mebby.domain.models.city.GeoDBModel
import retrofit2.Response
import retrofit2.http.GET

interface CityApi {
    @GET("cities?limit=10")
    suspend fun getCityList(): Response<GeoDBModel>
}