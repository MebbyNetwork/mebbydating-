package com.example.mebby.domain.useCases.authUseCases

import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.AuthRepository
import com.example.mebby.enums.AuthResponse
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithPhoneAuthCredentialUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun execute(credential: PhoneAuthCredential): Flow<Resource<AuthResponse>> {
        return repository.signInWithPhoneAuthCredential(credential = credential)
    }
}