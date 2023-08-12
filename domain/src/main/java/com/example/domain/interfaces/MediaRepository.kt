package com.example.domain.interfaces

import com.example.domain.Resource
import com.example.domain.models.PictureModel
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    suspend fun addPicture(picture: PictureModel): Flow<Resource<Boolean>>
}