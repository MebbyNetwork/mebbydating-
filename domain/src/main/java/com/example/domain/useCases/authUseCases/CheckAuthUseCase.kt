package com.example.domain.useCases.authUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.AuthRepository
import com.example.domain.sealed.AuthStates
import javax.inject.Inject

class CheckAuthUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(): Resource<AuthStates> {
        return repository.checkAuth()
    }
}