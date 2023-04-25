package com.example.mebby.domain.useCases.authUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun execute(): Resource<Boolean> {
        return repository.signOut()
    }
}