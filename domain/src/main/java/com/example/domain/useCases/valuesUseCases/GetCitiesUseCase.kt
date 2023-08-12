package com.example.domain.useCases.valuesUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.ValuesRepository
import com.example.domain.models.city.CityModel
import javax.inject.Inject

class GetCitiesUseCase @Inject constructor(
    private val repository: ValuesRepository
) {
    suspend fun execute(): Resource<List<CityModel>> {
        return repository.getCities()
    }
}