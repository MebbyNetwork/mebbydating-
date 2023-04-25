package com.example.mebby.domain.useCases.mediaUseCases

import com.example.mebby.domain.interfaces.StorageRepository
import javax.inject.Inject

class DeleteImageUseCase @Inject constructor(private val repository: StorageRepository) {
    suspend fun execute() {}
}