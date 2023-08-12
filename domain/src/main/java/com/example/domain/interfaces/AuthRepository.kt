package com.example.domain.interfaces

import com.example.domain.Resource
import com.example.domain.sealed.AuthStates
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendVerificationCode(phoneNumber: String, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
    suspend fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?, callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks)

    fun getCredentialForPhoneAuth(verificationId: String?, code: String): PhoneAuthCredential
    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Flow<Resource<AuthStates>>
    suspend fun checkAuth(): Resource<AuthStates>
    suspend fun signOut(): Resource<Boolean>
}