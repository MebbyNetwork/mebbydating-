package com.example.mebby.app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mebby.enums.GenderTypes
import com.example.mebby.data.Resource
import com.example.mebby.domain.models.ImageModel
import com.example.mebby.domain.models.InterestModel
import com.example.mebby.domain.models.UserProfileModel
import com.example.mebby.domain.models.city.CityModel
import com.example.mebby.domain.useCases.profileUseCases.EditProfileUseCase
import com.example.mebby.domain.useCases.sDataUseCases.GetInterestsListUseCase
import com.example.mebby.util.getTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getInterestsListUseCase: GetInterestsListUseCase,
    private val editProfileUseCase: EditProfileUseCase,
) : ViewModel() {
    init {
        getInterestsList()
    }

    private val _user = MutableLiveData<UserProfileModel>()
    val user: LiveData<UserProfileModel> get() = _user

    private val _interestsList = MutableLiveData<List<InterestModel>>()
    val interestsList: LiveData<List<InterestModel>> get() = _interestsList

    private val _changeState = MutableLiveData<Resource<Boolean>>()
    val changeState: LiveData<Resource<Boolean>> get() = _changeState

    fun changeUserProfile() {
        user.value?.let {
            viewModelScope.launch {
                editProfileUseCase.execute(it).collect() {
                    _changeState.postValue(it)
                }
            }
        }
    }

    fun setUser(user: UserProfileModel) {
        this._user.value = user
    }

    fun setName(username: String) {
        this._user.value = this._user.value?.copy(username =  username)
        Log.d("UpdatedUser", "${this._user.value}")
    }

    fun setBirthday(birthday: String) {
        val birthday = getTimestamp(birthday)
        this._user.value = this._user.value?.copy(birthday = birthday.time)
        Log.d("UpdatedUser", "${this._user.value}")
    }

    fun setAbout(about: String) {
        val about = about
        this._user.value = this._user.value?.copy(about = about)
        Log.d("UpdatedUser", "${this._user.value}")
    }

    fun setLocation(city: CityModel) {
        val city = city
        this._user.value = this._user.value?.copy(city = city)
        Log.d("UpdatedUser", "${this._user.value}")
    }

    fun setGender(gender: GenderTypes) {
        val gender = gender
        this._user.value = this._user.value?.copy(gender = gender.value)
        Log.d("UpdatedUser", "${this._user.value}")
    }


    fun setGeneralImage(image: ImageModel) {
        this._user.value = this._user.value?.copy(generalImage = image.uri)
    }

    fun addImage(image: ImageModel) {
        val newSelectList = _user.value?.profileImage?.let { ArrayList(it) }
        newSelectList?.add(0, image)

        this._user.value = this._user.value?.copy(profileImage = newSelectList)
    }

    private fun getInterestsList() {
        viewModelScope.launch {
            val result = getInterestsListUseCase.execute()

            Log.d("interestsListUseCase", "$result")

            when (result) {
                is Resource.Success -> {
                    _interestsList.postValue(result.data?.filter { _user.value?.interests?.contains(it) != true })
                }
                is Resource.Error -> {
                    Log.d("interestsListUseCase", "${result.message}")
                }
                else -> {}
            }
        }
    }

    fun addInterestInSelectedListInterests(value: InterestModel) {
        Log.d("addInterestInSelectedListInterests", "$value")


        if (user.value?.interests!!.contains(value)) return

        if (user.value?.interests!!.size < 5) {
            val newSelectList = _user.value?.interests?.let { ArrayList(it) }
            newSelectList?.add(value)
            this._user.value = newSelectList?.let { this._user.value?.copy(interests = it) }

            val newList = ArrayList(_interestsList.value!!)
            newList.remove(value)
            _interestsList.value = newList
        }
    }

    fun removeInterestInSelectedListInterests(value: InterestModel) {
        Log.d("removeInterestInSelectedListInterests", "${value}")

        if (_interestsList.value?.contains(value) == true) return

        val newSelectList = _user.value?.interests?.let { ArrayList(it) }
        newSelectList?.remove(value)
        this._user.value = this._user.value?.copy(interests = newSelectList!!.toList())

        val newList = ArrayList(_interestsList.value!!)
        newList.add(value)
        _interestsList.value = newList

        Log.d("removeInterestInSelectedListInterests", "${_user.value?.interests}")
    }
}