package com.example.mebby.domain.interfaces

import com.example.mebby.data.Resource
import com.example.mebby.enums.AuthResponse
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
    fun getCredentialForPhoneAuth(verificationId: String?, code: String): PhoneAuthCredential
    suspend fun sendVerificationCode(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)

    // Complete
    suspend fun checkAuth(): Resource<AuthResponse>
    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Flow<Resource<AuthResponse>>

    suspend fun signOut(): Resource<Boolean>
}