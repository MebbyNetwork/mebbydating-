package com.example.mebby.domain.interfaces

import com.example.mebby.data.Resource
import com.example.mebby.domain.models.ImageModel

interface StorageRepository {
    suspend fun addImage(imageModel: ImageModel): Resource<ImageModel>
    suspend fun addImages(list: List<ImageModel>): Resource<List<ImageModel>>
}