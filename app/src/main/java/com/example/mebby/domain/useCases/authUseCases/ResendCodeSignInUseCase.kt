package com.example.mebby.domain.useCases.authUseCases

import com.example.mebby.domain.interfaces.AuthRepository
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject

class ResendCodeSignInUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun execute(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        repository.resendVerificationCode(phoneNumber, token, callbacks)
    }
}