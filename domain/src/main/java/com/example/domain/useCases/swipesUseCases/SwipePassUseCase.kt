package com.example.domain.useCases.swipesUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.SwipesRepository
import javax.inject.Inject

class SwipePassUseCase @Inject constructor(
    private val repository: SwipesRepository
) {
    suspend operator fun invoke(userId: String): Resource<Boolean> {
        return repository.passUser(userId)
    }
}