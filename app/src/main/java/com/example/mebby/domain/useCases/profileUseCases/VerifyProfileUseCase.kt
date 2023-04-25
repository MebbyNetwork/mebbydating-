package com.example.mebby.domain.useCases.profileUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyProfileUseCase @Inject constructor(private val repository: ProfileRepository) {
    fun execute(userId: String, verificationType: String, verificationData: String): Flow<Resource<Boolean>> {
        return repository.verifyProfile(userId, verificationType, verificationData)
    }
}