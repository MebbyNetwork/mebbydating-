package com.example.mebby.domain.useCases.authUseCases

import com.example.mebby.domain.interfaces.AuthRepository
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject

class SendVerificationCodeUseCase @Inject constructor(private val repository: AuthRepository) {
   suspend fun execute(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks) {
        repository.sendVerificationCode(phoneNumber = phoneNumber, callbacks = callbacks)
    }
}