package com.example.domain.useCases.profileUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.ProfileRepository
import com.example.domain.models.ProfileModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(private val repository: ProfileRepository){
    suspend operator fun invoke(editedProfile: ProfileModel): Flow<Resource<Boolean>> {
        return if (editedProfile.isValid()) {
            repository.editProfile(editedProfile)
        } else {
            flowOf(Resource.Error(Exception("Profile is Invalid")))
        }
    }
}