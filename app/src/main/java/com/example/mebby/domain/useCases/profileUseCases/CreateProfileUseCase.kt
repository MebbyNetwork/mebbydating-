package com.example.mebby.domain.useCases.profileUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.FirestoreRepository
import com.example.mebby.domain.models.UserModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateProfileUseCase @Inject constructor(private val repository: FirestoreRepository) {
    suspend fun execute(user: UserModel): Flow<Resource<Boolean>> =
        repository.createUserProfile(user)
}
