package com.example.mebby.domain.useCases.sDataUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.FirestoreRepository
import com.example.mebby.domain.interfaces.RealtimeDatabaseRepository
import com.example.mebby.domain.models.InterestModel
import javax.inject.Inject

class GetInterestsListUseCase @Inject constructor(private val repository: RealtimeDatabaseRepository) {
    suspend fun execute(): Resource<List<InterestModel>> {
        return repository.getInterestsList()
    }
}