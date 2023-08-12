package com.example.mebby.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.models.InterestModel
import com.example.domain.models.PictureModel
import com.example.domain.models.ProfileModel
import com.example.domain.useCases.mediaUseCases.AddPictureUseCase
import com.example.domain.useCases.profileUseCases.ViewProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val viewProfileUseCase: ViewProfileUseCase,
    private val addPictureUseCase: AddPictureUseCase,
) : ViewModel() {
    private val _user = MutableLiveData<Resource<ProfileModel>>()
    val user: LiveData<Resource<ProfileModel>> get() = _user

    private val _interests = MutableLiveData<List<InterestModel>>()
    val interests: LiveData<List<InterestModel>> get() = _interests

    init {
        getUser()
    }

    fun getUser(uid: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            viewProfileUseCase(uid).collect {
                _user.postValue(it)
            }
        }
    }

    fun addPicture(picture: PictureModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPictureUseCase.execute(picture).collect() { picture ->
                when (picture) {
                    is Resource.Success -> {
                        getUser()
                    }
                    else -> {

                    }
                }
            }
        }
    }
}
