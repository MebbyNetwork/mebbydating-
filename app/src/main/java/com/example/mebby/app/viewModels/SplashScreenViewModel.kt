package com.example.mebby.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.sealed.AuthStates
import com.example.domain.useCases.authUseCases.CheckAuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val checkAuthUseCase: CheckAuthUseCase
) : ViewModel() {
    private val _authLiveData = MutableLiveData<Resource<AuthStates>>()
    val authLiveData: LiveData<Resource<AuthStates>> get() = _authLiveData

    init {
        checkAuth()
    }

    private fun checkAuth() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = checkAuthUseCase.execute()

            _authLiveData.postValue(result)
        }
    }
}
