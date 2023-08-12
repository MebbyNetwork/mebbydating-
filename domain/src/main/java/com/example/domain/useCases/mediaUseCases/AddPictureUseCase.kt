package com.example.domain.useCases.mediaUseCases

import com.example.domain.Resource
import com.example.domain.interfaces.MediaRepository
import com.example.domain.interfaces.ProfileRepository
import com.example.domain.models.PictureModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddPictureUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend fun execute(picture: PictureModel): Flow<Resource<Boolean>> {
        return repository.addPicture(picture = picture)
    }
}