package com.example.mebby.domain.useCases.sDataUseCases

import com.example.mebby.domain.interfaces.ApiRepository
import com.example.mebby.domain.models.city.CityModel
import javax.inject.Inject

class GetCitiesListUseCase @Inject constructor(private val repository: ApiRepository) {
    suspend fun execute(): List<CityModel> {
        return repository.getCitiesList()
    }
}