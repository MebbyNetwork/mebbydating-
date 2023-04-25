package com.example.mebby.data.repositories

import com.example.mebby.data.network.interfaces.CityApi
import com.example.mebby.domain.interfaces.ApiRepository
import com.example.mebby.domain.models.city.CityModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(private val cityApi: CityApi) : ApiRepository {
    override suspend fun getCitiesList(): List<CityModel> =
        withContext(Dispatchers.IO) {
            cityApi.getCityList().body()!!.data
        }
}
