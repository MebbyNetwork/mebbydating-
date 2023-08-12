package com.example.mebby.app.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.models.swipes.CardModel
import com.example.domain.models.swipes.SwipesCardsModel
import com.example.domain.useCases.swipesUseCases.GetSwipeCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val getSwipeCardsUseCase: GetSwipeCardsUseCase
): ViewModel() {
    private val mutableModelStream = MutableLiveData<SwipesCardsModel>()
    val modelStream: LiveData<SwipesCardsModel> get() = mutableModelStream

    private var currentIndex = 0

    private var cardModels: List<CardModel> = listOf()

    private val topCard
        get() = cardModels[currentIndex % cardModels.size]

    private val bottomCard
        get() = cardModels[(currentIndex + 1) % cardModels.size]

    init {
        viewModelScope.launch {
            getSwipeCardsUseCase.execute(null).collect() {
                when (it) {
                    is Resource.Success -> {
                        val data = it.data
                        if (data != null) { cardModels = data }

                        updateStream()
                    }
                    else -> {

                    }
                }
            }
        }
    }

    fun swipe() {
        currentIndex += 1

        updateStream()
    }

    private fun updateStream() {
        mutableModelStream.value = SwipesCardsModel(topCard = topCard, bottomCard = bottomCard)
    }
}