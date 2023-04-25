package com.example.mebby.domain.useCases.mediaUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.StorageRepository
import com.example.mebby.domain.models.ImageModel
import javax.inject.Inject

class UploadImagesUseCase @Inject constructor(private val repository: StorageRepository) {
    suspend fun execute(list: List<ImageModel>): Resource<List<ImageModel>> {
        return repository.addImages(list = list)
    }
}