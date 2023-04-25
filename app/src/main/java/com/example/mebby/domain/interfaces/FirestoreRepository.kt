package com.example.mebby.domain.interfaces

import com.example.mebby.data.Resource
import com.example.mebby.domain.models.ImageModel
import com.example.mebby.domain.models.UserModel
import com.example.mebby.domain.models.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface FirestoreRepository {
    suspend fun createUserProfile(user: UserModel): Flow<Resource<Boolean>>
    suspend fun getUserProfile(uid: String?): Flow<Resource<UserProfileModel>>

    suspend fun changeUserProfile(user: UserProfileModel): Flow<Resource<Boolean>>

    suspend fun addImages(images: List<ImageModel>): Flow<Resource<Boolean>>
}
