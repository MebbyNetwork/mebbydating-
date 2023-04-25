package com.example.mebby.domain.useCases.authUseCases

import com.example.mebby.domain.interfaces.AuthRepository
import com.google.firebase.auth.PhoneAuthCredential
import javax.inject.Inject

class GetCredentialForPhoneAuthUseCase @Inject constructor(private val repository: AuthRepository) {
    fun execute(verificationId: String?, code: String): PhoneAuthCredential {
        return repository.getCredentialForPhoneAuth(verificationId, code)
    }
}