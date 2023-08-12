package com.example.domain.useCases.authUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.AuthRepository
import com.example.domain.sealed.AuthStates
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithPhoneAuthCredentialUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(credential: PhoneAuthCredential): Flow<Resource<AuthStates>> {
        return repository.signInWithPhoneAuthCredential(credential)
    }
}