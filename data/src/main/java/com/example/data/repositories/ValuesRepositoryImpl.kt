package com.example.data.repositories

import com.example.data.interfaces.ValueStorage
import com.example.domain.Resource
import com.example.domain.interfaces.ValuesRepository
import com.example.domain.models.InterestModel
import com.example.domain.models.city.CityModel
import javax.inject.Inject

class ValuesRepositoryImpl @Inject constructor(
    private val storage: ValueStorage
): ValuesRepository {
    override suspend fun getCities(): Resource<List<CityModel>> {
        return storage.getCities()
    }

    override suspend fun getInterests(): Resource<List<InterestModel>> {
        return storage.getInterest()
    }
}