package com.example.data.repositories

import android.app.Activity
import com.example.data.constants.USERS_PATH
import com.example.data.utils.safeNetworkCall
import com.example.domain.Resource
import com.example.domain.interfaces.AuthRepository
import com.example.domain.sealed.AuthStates
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val activity: Activity,
    private val auth: FirebaseAuth
) : AuthRepository {
    override suspend fun sendVerificationCode(
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(50L, TimeUnit.SECONDS)
            .setActivity(activity.parent)
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
            .setActivity(activity.parent)
            .setCallbacks(callbacks)

        if (token != null) {
            optionsBuilder.setForceResendingToken(token)
        }

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    override fun getCredentialForPhoneAuth(
        verificationId: String?,
        code: String,
    ): PhoneAuthCredential {
        return PhoneAuthProvider.getCredential(verificationId!!, code)
    }

    override suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Flow<Resource<AuthStates>> {
        return callbackFlow {
            trySend(Resource.Loading())

            trySend(
                safeNetworkCall {
                    auth.signInWithCredential(credential).await()
                    checkAuth()
                }
            )
            close()

            awaitClose()
        }
    }

    override suspend fun checkAuth(): Resource<AuthStates> {
        return withContext(Dispatchers.IO) {
            if (auth.currentUser == null) {
                Resource.Success(AuthStates.IsNotLogged)
            } else {
                val reference = firestore
                    .collection(USERS_PATH)
                    .document(auth.currentUser!!.uid)
                    .get()
                    .await()

                if (reference.exists()) {
                    Resource.Success(AuthStates.IsRegistered)
                } else {
                    Resource.Success(AuthStates.IsNotRegistered)
                }
            }
        }
    }

    override suspend fun signOut(): Resource<Boolean> {
        return withContext(Dispatchers.IO) {
            safeNetworkCall {
                auth.signOut()
                Resource.Success(true)
            }
        }
    }
}