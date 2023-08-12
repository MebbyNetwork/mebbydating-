package com.example.domain.interfaces

import com.example.domain.Resource
import com.example.domain.models.InterestModel
import com.example.domain.models.city.CityModel

interface ValuesRepository {
    suspend fun getCities(): Resource<List<CityModel>>
    suspend fun getInterests(): Resource<List<InterestModel>>
}