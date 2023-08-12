package com.example.mebby.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.models.city.CityModel
import com.example.domain.useCases.valuesUseCases.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class CityPickerViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase
) : ViewModel() {

    init {
        getCities()
    }

    private val _cities = MutableLiveData<Resource<List<CityModel>>>()
    val cities: LiveData<Resource<List<CityModel>>> get() = _cities

    private fun getCities() {
        viewModelScope.launch {
            val cities = getCitiesUseCase.execute()
            _cities.postValue(cities)
        }
    }
}
