package com.example.data.repositories

import com.example.data.exceptions.CurrentUserIsInvalidException
import com.example.data.exceptions.UploadPictureFailedEception
import com.example.data.interfaces.MediaStorage
import com.example.data.utils.isUserInvalid
import com.example.data.utils.safeNetworkCall
import com.example.domain.Resource
import com.example.domain.interfaces.MediaRepository
import com.example.domain.models.PictureModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaStorage: MediaStorage,
    private val auth: FirebaseAuth
) : MediaRepository {
    override suspend fun addPicture(picture: PictureModel): Flow<Resource<Boolean>> {
        return callbackFlow {
            trySend(Resource.Loading())

            // Check if current user is signed in
            if (auth.isUserInvalid()) {
                trySend(Resource.Error(CurrentUserIsInvalidException()))
                close()
            }

            trySend(
                safeNetworkCall {
                    if (mediaStorage.uploadPicture(picture) is Resource.Success) {
                        Resource.Success(true)
                    } else {
                        Resource.Error(UploadPictureFailedEception("Something went wrong"))
                    }
                }
            )
            close()


            awaitClose {  }
        }
    }
}