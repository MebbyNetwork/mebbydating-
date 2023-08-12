package com.example.domain.useCases.swipesUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.SwipesRepository
import com.example.domain.models.swipes.CardModel
import com.example.domain.models.swipes.SwipesFiltersModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSwipeCardsUseCase @Inject constructor(
    private val repository: SwipesRepository
) {
    suspend fun execute(filters: SwipesFiltersModel? = null): Flow<Resource<List<CardModel>>> {
        return repository.getSwipeCards(filters)
    }
}