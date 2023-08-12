package com.example.data.interfaces

import com.example.domain.Resource
import com.example.domain.models.InterestModel
import com.example.domain.models.city.CityModel

interface ValueStorage {
    suspend fun getCities(): Resource<List<CityModel>>
    suspend fun getInterest(): Resource<List<InterestModel>>
}