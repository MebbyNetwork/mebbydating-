package com.example.mebby.domain.useCases.mediaUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.StorageRepository
import com.example.mebby.domain.models.ImageModel
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(private val repository: StorageRepository) {
    suspend fun execute(imageModel: ImageModel, index: Int): Resource<ImageModel> {
        return repository.addImage(imageModel)
    }
}