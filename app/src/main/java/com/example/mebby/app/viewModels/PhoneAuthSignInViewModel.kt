package com.example.mebby.app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mebby.data.Resource
import com.example.mebby.domain.states.PhoneAuthState
import com.example.mebby.domain.useCases.authUseCases.GetCredentialForPhoneAuthUseCase
import com.example.mebby.domain.useCases.authUseCases.ResendCodeSignInUseCase
import com.example.mebby.domain.useCases.authUseCases.SendVerificationCodeUseCase
import com.example.mebby.domain.useCases.authUseCases.SignInWithPhoneAuthCredentialUseCase
import com.example.mebby.enums.AuthResponse
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
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
    private val signInWithPhoneAuthCredentialUseCase: SignInWithPhoneAuthCredentialUseCase,
    private val getCredentialForPhoneAuthUseCase: GetCredentialForPhoneAuthUseCase,
    private val resendCodeSignInUseCase: ResendCodeSignInUseCase
) : ViewModel() {

    var phoneNumber = MutableLiveData<String>()
    var mutableStateSendCode = MutableLiveData(PhoneAuthState.DEFAULT)

    private var storedVerificationId = MutableLiveData("")
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    private val _authLiveData = MutableLiveData<Resource<AuthResponse>>()
    val authLiveData: LiveData<Resource<AuthResponse>> get() = _authLiveData

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
            Log.d("VERIFICATION", "onCodeSent:$verificationId")

            storedVerificationId.value = verificationId
            resendToken = token
            mutableStateSendCode.value = PhoneAuthState.SUCCESS
        }
    }

    fun changePhoneNumber(number: String) { phoneNumber.value = number }

    fun deleteExtraSymbols() = phoneNumber.value!!.replace("[()\\s-]".toRegex(), "")

    fun sendVerificationCode() {
        if (phoneNumber.value.toString().isNotEmpty()) {
            viewModelScope.launch {
                sendVerificationCodeUseCase.execute(deleteExtraSymbols(), callbacks)
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
            resendCodeSignInUseCase.execute(deleteExtraSymbols(), token, callbacks)
        }
    }

    fun getCredentialForPhoneAuth(
        verificationId: String?,
        code: String
    ) = getCredentialForPhoneAuthUseCase.execute(
        verificationId,
        code
    )

    fun getVerificationId(): String? = storedVerificationId.value

    fun getToken(): PhoneAuthProvider.ForceResendingToken = resendToken
}
