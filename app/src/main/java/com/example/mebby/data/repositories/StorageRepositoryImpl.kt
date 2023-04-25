package com.example.mebby.data.repositories

import android.util.Log
import androidx.core.net.toUri
import com.example.mebby.const.IMAGES_CHILD
import com.example.mebby.const.REQUEST_TAKE_PHOTO
import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.StorageRepository
import com.example.mebby.domain.models.ImageModel
import com.example.mebby.extensions.convertToSHA256
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage
) : StorageRepository {
    private val auth = Firebase.auth
    private val currentUser = auth.currentUser

    override suspend fun addImage(imageModel: ImageModel): Resource<ImageModel> =
        withContext(Dispatchers.IO) {
            val result = uploadImage(imageModel)
            Log.d(TAG, "${result.data}")
            result
        }

    override suspend fun addImages(list: List<ImageModel>): Resource<List<ImageModel>> =
        withContext(Dispatchers.IO) {
            try {
                val response = list.map {
                    CoroutineScope(Dispatchers.IO).async {
                        val result = uploadImage(imageModel = it)
                        Log.d(TAG, "$result")
                        result
                    }
                }.awaitAll()

                Resource.Success(response.mapNotNull { it.data })
            } catch (e: Exception) {
                Resource.Error(message = e.localizedMessage ?: "Something went wrong")
            }
        }

    private suspend fun uploadImage(imageModel: ImageModel): Resource<ImageModel> =
        withContext(Dispatchers.IO) {
            try {
                if (currentUser == null) {
                    return@withContext Resource.Error("Current user is null")
                } else {
                    val timestamp = Timestamp.now().seconds
                    val currentUid = currentUser.uid
                    val imageName = "${currentUid}_IMAGE_${timestamp}_${Random.nextBits(1332)}".convertToSHA256() + ".jpeg"
                    val reference = storage.reference.child("$IMAGES_CHILD/$currentUid/$imageName")
                    val metaData: StorageMetadata = StorageMetadata.Builder()
                        .setCustomMetadata("obtain", if (imageModel.request?.toInt() == REQUEST_TAKE_PHOTO) "Camera" else "Gallery")
                        .build()

                    val uploadTask = reference.putFile(imageModel.uri.toUri(), metaData).await().task

                    if (uploadTask.isSuccessful) {
                        val downloadUrl = reference.downloadUrl.await()

                        val image = ImageModel(
                            uri = downloadUrl.toString(),
                            request = imageModel.request,
                            generalImage = imageModel.generalImage,
                            timestamp = Timestamp.now().seconds
                        )

                        Resource.Success(image)
                    } else {
                        Resource.Error(message = uploadTask.exception?.localizedMessage ?: "Something went wrong")
                    }

                }
            } catch (e: Exception) {
                Resource.Error(message = e.localizedMessage ?: "Something went wrong")
            }
        }

    companion object {
        const val TAG = "StorageRepositoryImpl"
    }
}
