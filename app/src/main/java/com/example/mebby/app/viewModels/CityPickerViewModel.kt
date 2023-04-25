package com.example.mebby.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mebby.domain.models.city.CityModel
import com.example.mebby.domain.useCases.sDataUseCases.GetCitiesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CityPickerViewModel @Inject constructor(
    private val getCitiesListUseCase: GetCitiesListUseCase
) : ViewModel() {

    init {
        getCities()
    }

    private val mutableCityModelList = MutableLiveData<List<CityModel>>()
    val cityModelList: LiveData<List<CityModel>> get() = mutableCityModelList

    private fun getCities() {
        viewModelScope.launch {
            val cities = getCitiesListUseCase.execute()
            mutableCityModelList.postValue(cities)
        }
    }
}
