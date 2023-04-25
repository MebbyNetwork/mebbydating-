package com.example.mebby.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mebby.data.Resource
import com.example.mebby.domain.models.ImageModel
import com.example.mebby.domain.models.InterestModel
import com.example.mebby.domain.models.UserProfileModel
import com.example.mebby.domain.useCases.mediaUseCases.AddImageUseCase
import com.example.mebby.domain.useCases.profileUseCases.GetProfileUseCase
import com.squareup.picasso.Picasso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val addImageUseCase: AddImageUseCase,
) : ViewModel() {
    private val _user = MutableLiveData<Resource<UserProfileModel>>()
    val user: LiveData<Resource<UserProfileModel>> get() = _user

    private val _interests = MutableLiveData<List<InterestModel>>()
    val interests: LiveData<List<InterestModel>> get() = _interests

    init {
        getUser()
    }

    fun getUser(uid: String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            val userValue = _user.value?.data

            getProfileUseCase.execute(uid).collect {
                if (true) {
                    Picasso.get().load(it.data?.generalImage).fetch()

                    delay(500)
                    _user.postValue(it)
                }
            }
        }
    }

    fun updateUser(user: UserProfileModel) {
        _user.value = Resource.Success(user)
    }

    fun addImage(image: ImageModel) {
        viewModelScope.launch {
            addImageUseCase.execute(image).collect() {
                when (it) {
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
