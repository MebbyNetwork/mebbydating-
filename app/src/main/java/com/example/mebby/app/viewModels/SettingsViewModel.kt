package com.example.mebby.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mebby.data.Resource
import com.example.mebby.domain.useCases.authUseCases.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase
): ViewModel() {
    private val _auth = MutableLiveData<Resource<Boolean>?>()
    val auth: LiveData<Resource<Boolean>?> = _auth


    fun signOutUseCase() {
        viewModelScope.launch(Dispatchers.IO) {
            _auth.postValue(signOutUseCase.execute())
            delay(1000)
            _auth.postValue(null)
        }
    }
}