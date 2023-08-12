package com.example.domain.useCases.swipesUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.SwipesRepository
import com.example.domain.models.swipes.SwipesFiltersModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSwipeFiltersUseCase @Inject constructor(
    private val swipesRepository: SwipesRepository
) {
    suspend fun execute(): Flow<Resource<SwipesFiltersModel>> {
        return swipesRepository.getSwipeFilters()
    }
}