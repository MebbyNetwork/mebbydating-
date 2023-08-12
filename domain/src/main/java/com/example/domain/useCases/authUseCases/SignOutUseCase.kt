package com.example.domain.useCases.authUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(): Resource<Boolean> {
        return repository.signOut()
    }
}