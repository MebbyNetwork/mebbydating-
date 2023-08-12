package com.example.mebby.app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.sealed.AuthStates
import com.example.domain.useCases.authUseCases.GetCredentialForPhoneAuthUseCase
import com.example.domain.useCases.authUseCases.ResendCodeUseCase
import com.example.domain.useCases.authUseCases.SendCodeUseCase
import com.example.domain.useCases.authUseCases.SignInWithPhoneAuthCredentialUseCase
import com.example.mebby.util.SingleLiveEvent
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneAuthSignInViewModel @Inject constructor(
    private val sendCodeUseCase: SendCodeUseCase,
    private val signInWithPhoneAuthCredentialUseCase: SignInWithPhoneAuthCredentialUseCase,
    private val getCredentialForPhoneAuthUseCase: GetCredentialForPhoneAuthUseCase,
    private val resendCodeUseCase: ResendCodeUseCase
) : ViewModel() {

    var phoneNumber = MutableLiveData<String>()
    var mutableStateSendCode = SingleLiveEvent<Boolean>()

    private var storedVerificationId = MutableLiveData("")
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private val _authLiveData = MutableLiveData<Resource<AuthStates>>()
    val authLiveData: LiveData<Resource<AuthStates>> get() = _authLiveData

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w("VERIFICATION", "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {} else if (e is FirebaseTooManyRequestsException) {}
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            storedVerificationId.value = verificationId
            resendToken = token
            mutableStateSendCode.value = true
        }
    }

    fun changePhoneNumber(number: String) { phoneNumber.value = number }

    fun deleteExtraSymbols() = phoneNumber.value!!.replace("[()\\s-]".toRegex(), "")

    fun sendVerificationCode() {
        if (phoneNumber.value.toString().isNotEmpty()) {
            viewModelScope.launch {
                sendCodeUseCase.execute(deleteExtraSymbols(), callbacks)
            }
        }
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        viewModelScope.launch {
            signInWithPhoneAuthCredentialUseCase.execute(credential).collect {
                _authLiveData.postValue(it)
            }
        }
    }

    fun resendVerificationCode(token: PhoneAuthProvider.ForceResendingToken?) {
        viewModelScope.launch {
            resendCodeUseCase.execute(deleteExtraSymbols(), token, callbacks)
        }
    }

    fun getCredentialForPhoneAuth(verificationId: String, code: String) =
            getCredentialForPhoneAuthUseCase.execute(verificationId, code)

    fun getVerificationId(): String? = storedVerificationId.value

    fun getToken(): PhoneAuthProvider.ForceResendingToken = resendToken
}
