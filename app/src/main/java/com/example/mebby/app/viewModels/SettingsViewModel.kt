package com.example.mebby.app.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.useCases.authUseCases.SignOutUseCase
import com.example.mebby.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase
): ViewModel() {
    private val _auth = SingleLiveEvent<Resource<Boolean>?>()
    val auth: SingleLiveEvent<Resource<Boolean>?> = _auth


    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            _auth.postValue(signOutUseCase.execute())
            delay(1000)
            _auth.postValue(null)
        }
    }
}