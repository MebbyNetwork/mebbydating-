package com.example.mebby.domain.useCases.profileUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.FirestoreRepository
import com.example.mebby.domain.models.UserModel
import com.example.mebby.domain.models.UserProfileModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val repository: FirestoreRepository) {
    suspend fun execute(uid: String? = null): Flow<Resource<UserProfileModel>> {
        return repository.getUserProfile(uid = uid)
    }
}