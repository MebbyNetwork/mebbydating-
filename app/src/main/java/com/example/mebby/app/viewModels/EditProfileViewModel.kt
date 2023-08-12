package com.example.mebby.app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.models.InterestModel
import com.example.domain.models.PictureModel
import com.example.domain.models.ProfileModel
import com.example.domain.models.city.CityModel
import com.example.domain.sealed.Gender
import com.example.domain.useCases.profileUseCases.EditProfileUseCase
import com.example.domain.useCases.valuesUseCases.GetInterestUseCase
import com.example.mebby.util.SingleLiveEvent
import com.example.mebby.util.getTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getInterestsUseCase: GetInterestUseCase,
    private val editProfileUseCase: EditProfileUseCase,
) : ViewModel() {
    init {
        getInterestsList()
    }

    private val _user = MutableLiveData<ProfileModel>()
    val user: LiveData<ProfileModel> get() = _user

    private val _interests = MutableLiveData<List<InterestModel>>()
    val interestsList: LiveData<List<InterestModel>> get() = _interests

    private val _changeState = SingleLiveEvent<Resource<Boolean>>()
    val changeState: SingleLiveEvent<Resource<Boolean>> get() = _changeState

    fun changeUserProfile() {
        user.value?.let {
            viewModelScope.launch {
                editProfileUseCase.invoke(it).collect() {
                    _changeState.postValue(it)
                }
            }
        }
    }

    fun setUser(user: ProfileModel) {
        this._user.value = user
    }

    fun setName(username: String) {
        this._user.value = this._user.value?.copy(username =  username)
    }

    fun setBirthday(birthday: String) {
        this._user.value = this._user.value?.copy(birthday = getTimestamp(birthday).time)
    }

    fun setAbout(about: String) {
        this._user.value = this._user.value?.copy(about = about)
    }

    fun setLocation(city: CityModel) {
        val city = city
        this._user.value = this._user.value?.copy(city = city)
    }

    fun setGender(gender: Gender) {
        this._user.value = this._user.value?.copy(genderType = gender.value)
    }


    fun setGeneralImage(picture: PictureModel) {
        this._user.value = this._user.value?.copy(pictureProfile = picture)
    }

    fun addImage(picture: PictureModel) {
        val newSelectList = _user.value?.pictures?.let { ArrayList(it) }
        newSelectList?.add(0, picture)

        this._user.value = this._user.value?.copy(pictures = newSelectList?.toList() ?: listOf())
    }

    private fun getInterestsList() {
        viewModelScope.launch {
            val result = getInterestsUseCase.execute()

            when (result) {
                is Resource.Success -> {
                    _interests.postValue(result.data?.filter { _user.value?.interests?.contains(it) != true })
                }
                is Resource.Error -> {
                    Log.d("interestsListUseCase", "${result.exception}")
                }
                else -> {}
            }
        }
    }

    fun selectInterest(value: InterestModel) {
        if (user.value?.interests!!.contains(value)) return

        if (user.value?.interests!!.size < 5) {
            val newSelectList = _user.value?.interests?.let { ArrayList(it) }
            newSelectList?.add(value)
            this._user.value = newSelectList?.let { this._user.value?.copy(interests = it) }

            val newList = ArrayList(_interests.value!!)
            newList.remove(value)
            _interests.value = newList
        }
    }

    fun unselectInterest(value: InterestModel) {
        if (_interests.value?.contains(value) == true) return

        val newSelectList = _user.value?.interests?.let { ArrayList(it) }
        newSelectList?.remove(value)
        this._user.value = this._user.value?.copy(interests = newSelectList!!.toList())

        val newList = ArrayList(_interests.value!!)
        newList.add(value)
        _interests.value = newList
    }
}