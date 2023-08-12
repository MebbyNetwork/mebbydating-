package com.example.mebby.app.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.models.InterestModel
import com.example.domain.models.city.CityModel
import com.example.domain.models.swipes.AgeRange
import com.example.domain.models.swipes.SwipesFiltersModel
import com.example.domain.sealed.Find
import com.example.domain.sealed.Gender
import com.example.domain.sealed.Show
import com.example.domain.useCases.swipesUseCases.GetSwipeFiltersUseCase
import com.example.domain.useCases.swipesUseCases.UpdateSwipeFiltersUseCase
import com.example.domain.useCases.valuesUseCases.GetInterestUseCase
import com.example.mebby.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwipeFilterViewModel @Inject constructor(
    private val getInterestUseCase: GetInterestUseCase,
    private val getSwipeFiltersUseCase: GetSwipeFiltersUseCase,
    private val updateSwipeFiltersUseCase: UpdateSwipeFiltersUseCase,

) : ViewModel() {
    val _selectedInterests = MutableLiveData<List<InterestModel>>(mutableListOf())
    val selectedInterest: LiveData<List<InterestModel>> get() = _selectedInterests

    private val _interests = MutableLiveData<List<InterestModel>>()
    val interests: LiveData<List<InterestModel>> get() = _interests

    private val _filters = MutableLiveData<SwipesFiltersModel>()
    val filters: LiveData<SwipesFiltersModel> get() = _filters

    private val _changeState = SingleLiveEvent<Resource<Boolean>>()
    val changeState: SingleLiveEvent<Resource<Boolean>> get() = _changeState

    fun selectInterest(value: InterestModel) {
        Log.d("initInterestRecyclerView", "_interest = ${filters.value?.interest!!.contains(value)}")
        if (filters.value?.interest!!.contains(value)) return

        if (filters.value?.interest!!.size < 5) {
            val newSelectList = filters.value?.interest?.let { ArrayList(it) }
            newSelectList?.add(value)
            this._filters.value = newSelectList?.let { this._filters.value?.copy(interest = it) }

            val newList = ArrayList(_interests.value!!)
            newList.remove(value)
            _interests.value = newList

            Log.d("initInterestRecyclerView", "_interest = ${_interests}")
            Log.d("initInterestRecyclerView", "_filters = ${_filters.value?.interest}")
        }
    }

    fun unselectInterest(value: InterestModel) {
        if (_interests.value?.contains(value) == true) return

        val newSelectList = _filters.value?.interest?.let { ArrayList(it) }
        newSelectList?.remove(value)
        this._filters.value = this._filters.value?.copy(interest = newSelectList!!.toList())

        val newList = ArrayList(_interests.value!!)
        newList.add(value)
        _interests.value = newList

        Log.d("initInterestRecyclerView", "_interest = $_interests")
        Log.d("initInterestRecyclerView", "_filters = ${_filters.value?.interest}")
    }

    fun changeAgeRange(list: List<Float>) {
        val ageRange = AgeRange(
            startAge = list[0].toInt(),
            endAge = list[1].toInt(),
        )

        _filters.value = _filters.value?.copy(ageRange = ageRange)
    }

    fun changeShow(show: Show) {
        _filters.value = _filters.value?.copy(show = show.value)
    }

    fun changeFind(find: Find) {
        _filters.value = _filters.value?.copy(find = find.value)
    }

    fun changeLocation(location: CityModel) {
        _filters.value = _filters.value?.copy(location = location)
    }

    fun getSwipeFilters(callback: (Resource<SwipesFiltersModel>) -> Unit) {
        viewModelScope.launch {
            getSwipeFiltersUseCase.execute().collect() {
                callback(it)
            }
         }
    }

    fun initInterests() {
        viewModelScope.launch {
            val result = getInterestUseCase.execute()
            if (result is Resource.Success) {
                _interests.postValue(result.data?.filter { _filters.value?.interest?.contains(it) != true })
            }
        }
    }

    fun initFilters(filters: SwipesFiltersModel) {
        _filters.value = filters
    }

    fun updateFilters() {
        viewModelScope.launch {
            _filters.value?.let { it ->
                updateSwipeFiltersUseCase.execute(filters = it).collect() { resource ->
                    _changeState.postValue(resource)
                }
            }
        }

    }
}