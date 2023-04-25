package com.example.mebby.data.repositories

import com.example.mebby.const.ACTIVITY
import com.example.mebby.const.USERS_CHILD
import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.AuthRepository
import com.example.mebby.enums.AuthResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : AuthRepository {
    private val auth: FirebaseAuth = Firebase.auth

    override suspend fun sendVerificationCode(
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(50L, TimeUnit.SECONDS)
            .setActivity(ACTIVITY)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken?,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(50L, TimeUnit.SECONDS)
            .setActivity(ACTIVITY)
            .setCallbacks(callbacks)

        if (token != null) {
            optionsBuilder.setForceResendingToken(token)
        }

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    override fun getCredentialForPhoneAuth(
        verificationId: String?,
        code: String,
    ): PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId!!, code)

    override suspend fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
    ): Flow<Resource<AuthResponse>> = callbackFlow {

        trySend(Resource.Loading())

        try {
            auth.signInWithCredential(credential).await()
            trySend(checkAuth())
            close()
        } catch (e: Exception) {
            trySend(Resource.Error(message = e.localizedMessage ?: "Something went wrong"))
            close()
        }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    override suspend fun checkAuth(): Resource<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val user = auth.currentUser

                if (user == null) {
                    Resource.Success(AuthResponse.UserIsNotLoggedIn)
                } else {
                    val uid = user.uid
                    val reference = firestore.collection(USERS_CHILD).document(uid).get().await()

                    if (reference.exists()) {
                        Resource.Success(AuthResponse.UserIsRegistered)
                    } else {
                        Resource.Success(AuthResponse.UserIsNotRegistered)
                    }
                }
            } catch (e: Exception) {
                Resource.Error(message = e.localizedMessage ?: "Something went wrong", data = null)
            }
        }
    }

    override suspend fun signOut(): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                auth.signOut()
                Resource.Success(true)
            } catch (e: Exception) {
                Resource.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }
}
