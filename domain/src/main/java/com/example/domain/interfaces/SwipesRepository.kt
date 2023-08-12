package com.example.domain.interfaces

import com.example.domain.Resource
import com.example.domain.models.swipes.CardModel
import com.example.domain.models.swipes.SwipesFiltersModel
import kotlinx.coroutines.flow.Flow

interface SwipesRepository {
    suspend fun getSwipeCards(filters: SwipesFiltersModel?): Flow<Resource<List<CardModel>>>

    suspend fun getSwipeFilters(): Flow<Resource<SwipesFiltersModel>>
    suspend fun updateSwipeFilters(filters: SwipesFiltersModel): Flow<Resource<Boolean>>

    suspend fun likeUser(userId: String): Resource<Boolean>
    suspend fun passUser(userId: String): Resource<Boolean>
    suspend fun undoSwipe(userId: String): Resource<Boolean>
    suspend fun getMatchedProfilesUseCase(): Flow<Resource<String>>
}