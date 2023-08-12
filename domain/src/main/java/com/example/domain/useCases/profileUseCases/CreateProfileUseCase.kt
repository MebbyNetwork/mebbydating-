package com.example.domain.useCases.profileUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.ProfileRepository
import com.example.domain.models.ProfileModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CreateProfileUseCase(private val repository: ProfileRepository) {
    suspend operator fun invoke(profile: ProfileModel): Flow<Resource<Boolean>> {
        return if (profile.isValid()) {
            repository.createProfile(profile)
        } else {
            flowOf(Resource.Error(Exception("Invalid profile Data")))
        }
    }
}
