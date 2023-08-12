package com.example.domain.useCases.authUseCases

import com.example.domain.interfaces.AuthRepository
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject

class SendCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun execute(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        repository.sendVerificationCode(phoneNumber, callbacks)
    }
}