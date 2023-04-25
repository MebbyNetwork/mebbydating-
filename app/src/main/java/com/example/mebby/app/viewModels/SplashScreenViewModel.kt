package com.example.mebby.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mebby.data.Resource
import com.example.mebby.domain.useCases.authUseCases.CheckAuthUseCase
import com.example.mebby.enums.AuthResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val checkAuthUseCase: CheckAuthUseCase
) : ViewModel() {
    private val _authLiveData = MutableLiveData<Resource<AuthResponse>>()
    val authLiveData: LiveData<Resource<AuthResponse>> get() = _authLiveData

    fun checkAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            _authLiveData.postValue(checkAuthUseCase.execute())
        }
    }
}
