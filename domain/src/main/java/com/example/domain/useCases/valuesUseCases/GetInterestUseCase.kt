package com.example.domain.useCases.valuesUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.ValuesRepository
import com.example.domain.models.InterestModel
import javax.inject.Inject

class GetInterestUseCase @Inject constructor(
    private val repository: ValuesRepository
){
    suspend fun execute(): Resource<List<InterestModel>> {
        return repository.getInterests()
    }
}