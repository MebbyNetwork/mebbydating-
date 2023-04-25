package com.example.mebby.app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mebby.util.getTimestamp
import com.example.mebby.enums.FindTypes
import com.example.mebby.enums.GenderTypes
import com.example.mebby.enums.ShowTypes
import com.example.mebby.data.Resource
import com.example.mebby.domain.models.ImageModel
import com.example.mebby.domain.models.InterestModel
import com.example.mebby.domain.models.UserModel
import com.example.mebby.domain.models.city.CityModel
import com.example.mebby.domain.useCases.profileUseCases.CreateProfileUseCase
import com.example.mebby.domain.useCases.sDataUseCases.GetInterestsListUseCase
import com.example.mebby.extensions.swap
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val interestsListUseCase: GetInterestsListUseCase,
    private val createProfileUseCase: CreateProfileUseCase
) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _birthday = MutableLiveData("dd/mm/yyyy")
    val birthday: LiveData<String> get() = _birthday

    private val _gender = MutableLiveData<GenderTypes>()
    val gender: LiveData<GenderTypes> get() = _gender

    private val _find = MutableLiveData<FindTypes>()
    val find: LiveData<FindTypes> get() = _find

    private val _show = MutableLiveData<ShowTypes>()
    val show: LiveData<ShowTypes> get() = _show

    private val _images = MutableLiveData<List<ImageModel>>()
    val images: LiveData<List<ImageModel>> get() = _images

    private val _selectedInterests = MutableLiveData<List<InterestModel>>()
    val selectedInterest: LiveData<List<InterestModel>> get() = _selectedInterests

    private val _about = MutableLiveData<String>()
    val about: LiveData<String> get() = _about

    private val _city = MutableLiveData<CityModel>()
    val city: LiveData<CityModel> get() = _city

    private val _interests = MutableLiveData<List<InterestModel>>()
    val interests: LiveData<List<InterestModel>> get() = _interests

    init {
        viewModelScope.launch {
            val result = interestsListUseCase.execute()

            Log.d("interestsListUseCase", "$result")

            when (result) {
                is Resource.Success -> {
                    _interests.postValue(result.data!!)
                }
                is Resource.Error -> {
                    Log.d("interestsListUseCase", "${result.message}")
                }
                else -> {

                }
            }
        }

        _images.value = mutableListOf()
        _selectedInterests.value = mutableListOf()
    }

    fun setFirstName(value: String) {
        _name.value = value
    }

    fun setGenderValue(value: GenderTypes) {
        _gender.value = value
    }

    fun setFindValue(value: FindTypes) {
        _find.value = value
    }

    fun setShowValue(value: ShowTypes) {
        _show.value = value
    }

    fun addImageInList(value: ImageModel) {
        val newList: List<ImageModel> = _images.value!!.plus(value)
        _images.value = newList
    }

    fun deleteImageInList(value: ImageModel) {
        val newList = ArrayList(_images.value!!)
        newList.remove(value)
        _images.value = newList
    }

    fun swapImageInList(sourcePosition: Int, targetPosition: Int) {
        val newListImageModel: List<ImageModel> = _images.value!!
        val list = newListImageModel as MutableList
        list.swap(sourcePosition, targetPosition)
        _images.value = list
    }

    fun addInterestInSelectedListInterests(value: InterestModel) {
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

    fun removeInterestInSelectedListInterests(value: InterestModel) {
        if (_interests.value?.contains(value) == true) return

        val newSelectList = ArrayList(_selectedInterests.value!!)
        newSelectList.remove(value)
        _selectedInterests.value = newSelectList

        val newList = ArrayList(_interests.value!!)
        newList.add(value)
        _interests.value = newList
    }

    fun setAboutMeInformation(value: String) {
        _about.value = value
    }

    fun setCity(value: CityModel) {
        _city.value = value
    }

    fun setDay(value: String) {
        val string = "dd".replaceRange(0, value.length, value)
        _birthday.value = _birthday.value!!.replaceRange(0, 2, string)
    }

    fun setMonth(value: String) {
        val string = "mm".replaceRange(0, value.length, value)
        _birthday.value = _birthday.value!!.replaceRange(3, 5, string)
    }

    fun setYear(value: String) {
        val string = "yyyy".replaceRange(0, value.length, value)
        _birthday.value = _birthday.value!!.replaceRange(6, 10, string)
    }

    private var _authLiveData =  MutableLiveData<Resource<Boolean>>()
    val authLiveData: LiveData<Resource<Boolean>> get() = _authLiveData

    fun createUser() {
        viewModelScope.launch {
            try {
                val user = UserModel(
                    name = name.value!!,
                    birthday = getTimestamp(birthday.value!!),
                    gender = gender.value!!,
                    find = find.value!!,
                    show = show.value!!,
                    images = images.value!!,
                    interest = selectedInterest.value!!,
                    about = about.value!!,
                    city = city.value!!
                )

                createProfileUseCase.execute(user).collect {
                    Log.d("createUserUseCase", "$it")
                    _authLiveData.value = it
                }
            } catch (e: Exception) {
                _authLiveData.value = Resource.Error(e.localizedMessage ?: "Something went wrong")
            }
        }
    }

    companion object {
        // Constants
        const val photoLengthMaxSize = 5
    }
}
