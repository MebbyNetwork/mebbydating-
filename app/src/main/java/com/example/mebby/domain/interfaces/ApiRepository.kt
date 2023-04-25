package com.example.mebby.domain.interfaces

import com.example.mebby.domain.models.city.CityModel

interface ApiRepository {
    suspend fun getCitiesList(): List<CityModel>
}