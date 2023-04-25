package com.example.mebby.domain.useCases.mediaUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.FirestoreRepository
import com.example.mebby.domain.models.ImageModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddImageUseCase @Inject constructor(private val repository: FirestoreRepository) {
    suspend fun execute(image: ImageModel): Flow<Resource<Boolean>>  {
        return repository.addImages(listOf(image))
    }

    suspend fun execute(image: List<ImageModel>): Flow<Resource<Boolean>> {
        return repository.addImages(image)
    }
}