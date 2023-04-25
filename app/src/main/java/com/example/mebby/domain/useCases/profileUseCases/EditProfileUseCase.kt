package com.example.mebby.domain.useCases.profileUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.FirestoreRepository
import com.example.mebby.domain.models.UserProfileModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(private val repository: FirestoreRepository) {
    suspend fun execute(user: UserProfileModel): Flow<Resource<Boolean>> {
        return repository.changeUserProfile(user)
    }
}