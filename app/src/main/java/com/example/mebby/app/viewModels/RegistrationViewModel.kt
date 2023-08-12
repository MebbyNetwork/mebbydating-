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
import com.example.domain.sealed.Find
import com.example.domain.sealed.Gender
import com.example.domain.sealed.Show
import com.example.domain.useCases.profileUseCases.CreateProfileUseCase
import com.example.domain.useCases.valuesUseCases.GetInterestUseCase
import com.example.mebby.util.getTimestamp
import com.example.mebby.extensions.swap
import com.example.mebby.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val getInterestUseCase: GetInterestUseCase,
    private val createProfileUseCase: CreateProfileUseCase
) : ViewModel() {
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _birthday = MutableLiveData("dd/mm/yyyy")
    val birthday: LiveData<String> get() = _birthday

    private val _gender = MutableLiveData<Gender>()
    val gender: LiveData<Gender> get() = _gender

    private val _find = MutableLiveData<Find>()

    private val _show = MutableLiveData<Show>()
    val show: LiveData<Show> get() = _show

    private val _images = MutableLiveData<List<PictureModel>>(mutableListOf())
    val images: LiveData<List<PictureModel>> get() = _images

    private val _selectedInterests = MutableLiveData<List<InterestModel>>(mutableListOf())
    val selectedInterest: LiveData<List<InterestModel>> get() = _selectedInterests

    private val _about = MutableLiveData<String>()
    val about: LiveData<String> get() = _about

    private val _city = MutableLiveData<com.example.domain.models.city.CityModel>()
    val city: LiveData<com.example.domain.models.city.CityModel> get() = _city

    private val _interests = MutableLiveData<List<InterestModel>>()
    val interests: LiveData<List<InterestModel>> get() = _interests

    init {
        getInterests()
    }

    fun setFirstName(value: String) { _name.value = value }

    fun setGender(value: Gender) { _gender.value = value }

    fun setFind(value: Find) { _find.value = value }

    fun setShow(value: Show) { _show.value = value }

    fun addPictureInList(value: PictureModel) {
        val newList: List<PictureModel> = _images.value!!.plus(value)
        _images.value = newList
    }

    fun deletePictureFromList(value: PictureModel) {
        val newList = ArrayList(_images.value!!)
        newList.remove(value)
        _images.value = newList
    }

    fun swapPicture(sourcePosition: Int, targetPosition: Int) {
        val newListImageModel: List<PictureModel> = _images.value!!
        val list = newListImageModel as MutableList
        list.swap(sourcePosition, targetPosition)
        _images.value = list
    }

    fun selectInterest(value: InterestModel) {
        if (_selectedInterests.value?.contains(value) == true) return

        if (_selectedInterests.value?.size!! < photoLengthMaxSize) {
            val newSelectList = ArrayList(_selectedInterests.value!!)
            newSelectList.add(value)
            _selectedInterests.value = newSelectList

            val newList = ArrayList(_interests.value!!)
            newList.remove(value)
            _interests.value = newList
        }
    }

    fun unselectInterest(value: InterestModel) {
        if (_interests.value?.contains(value) == true) return

        val newSelectList = ArrayList(_selectedInterests.value!!)
        newSelectList.remove(value)
        _selectedInterests.value = newSelectList

        val newList = ArrayList(_interests.value!!)
        newList.add(value)
        _interests.value = newList
    }

    fun setAbout(value: String) { _about.value = value }

    fun setCity(value: com.example.domain.models.city.CityModel) { _city.value = value }

    fun setBirthday(birthday: String) { _birthday.value = birthday }

    private fun getInterests() {
        viewModelScope.launch {
            when (val result = getInterestUseCase.execute()) {
                is Resource.Success -> {
                    _interests.postValue(result.data!!)
                }
                is Resource.Error -> {
                    Log.d("getInterestUseCase", "${result.exception?.message}")
                }
                else -> {}
            }
        }
    }

    private var _authLiveData =  SingleLiveEvent<Resource<Boolean>>()
    val authLiveData: LiveData<Resource<Boolean>> get() = _authLiveData

    fun createProfile() {
        viewModelScope.launch {
            val user = ProfileModel(
                username = name.value!!,
                about = about.value!!,

                birthday = getTimestamp(birthday.value!!).time,
                city = city.value!!,

                genderType = _gender.value!!.value,
                findType = _find.value!!.value,
                showType = show.value!!.value,

                pictures = images.value!!,
                pictureProfile = images.value!![0],
                interests = selectedInterest.value!!,
            )

            createProfileUseCase.invoke(user).collect { data ->
                _authLiveData.value = data
            }
        }
    }

    companion object {
        // Constants
        const val photoLengthMaxSize = 5
    }
}
