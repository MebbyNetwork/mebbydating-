package com.example.domain.useCases.profileUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.ProfileRepository
import com.example.domain.models.ProfileModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViewProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
    suspend operator fun invoke(userId: String? = null): Flow<Resource<ProfileModel>> {
        return repository.viewProfile(userId)
    }
}