package com.example.mebby.domain.useCases.authUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.AuthRepository
import com.example.mebby.enums.AuthResponse
import javax.inject.Inject

class CheckAuthUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun execute(): Resource<AuthResponse> {
        return repository.checkAuth()
    }
}
