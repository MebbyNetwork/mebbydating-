package com.example.data.storages

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.data.constants.MediaStorageConstant
import com.example.data.exceptions.CurrentUserIsInvalidException
import com.example.data.interfaces.MediaStorage
import com.example.data.utils.isUserInvalid
import com.example.data.utils.safeNetworkCall
import com.example.domain.Resource
import com.example.domain.models.PictureModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class MediaStorageImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth,
    private val context: Context,
) : MediaStorage {
    override suspend fun uploadPicture(picture: PictureModel): Resource<PictureModel> {
        if (auth.isUserInvalid()) {
            return Resource.Error(CurrentUserIsInvalidException())
        }
        return safeNetworkCall {
            val userId = auth.currentUser!!.uid

            // Compress picture
            val imageUri = picture.imageUrl.toUri()
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)

            // Resize picture
            val width = bitmap.width
            val height = bitmap.height
            val scale = 0.5f // adjust this value as per your requirement
            val matrix = Matrix()
            matrix.postScale(scale, scale)
            val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false)

            val bytes = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.WEBP, 50, bytes)

            // Upload image to storage
            val randomUUID = UUID.randomUUID().toString()
            val pictureName = "$randomUUID.webp"
            val storageReference = storage
                .reference
                .child("${MediaStorageConstant.PICTURES}/$userId/$pictureName")

            storageReference.putBytes(bytes.toByteArray()).await()
            bytes.flush()
            bytes.close()

            val downloadUrl = storageReference.downloadUrl.await().toString()

            // Add data about picture in Firestore
            val pictureModel = picture.copy(
                pictureId = randomUUID,
                userId = userId,
                imageUrl = downloadUrl,
                uploadDate = Timestamp.now().seconds,
                isProfilePicture = picture.isProfilePicture
            )

            firestore
                .collection(MediaStorageConstant.PICTURES)
                .document(pictureModel.pictureId!!)
                .set(pictureModel)
                .await()

            Resource.Success(pictureModel)
        }
    }

    override suspend fun deletePicture(pictureId: String): Resource<Boolean> {
        if (auth.isUserInvalid()) {
            return Resource.Error(CurrentUserIsInvalidException())
        }

        return safeNetworkCall {
            val userId = auth.currentUser!!.uid

            // Delete picture from Storage
            storage
                .reference
                .child("${MediaStorageConstant.PICTURES}/$userId/$pictureId.jpeg")
                .delete()
                .await()

            // Delete picture data from Firestore
            firestore
                .collection(MediaStorageConstant.PICTURES)
                .document(pictureId)
                .delete()
                .await()

            Resource.Success(true)
        }
    }

    override suspend fun getProfilePictures(userId: String): Resource<List<PictureModel>> {
        if (auth.isUserInvalid()) {
            return Resource.Error(CurrentUserIsInvalidException())
        }

        return safeNetworkCall {
            val querySnapshot = firestore
                .collection(MediaStorageConstant.PICTURES)
                .whereEqualTo("userId", userId)
                .orderBy("uploadDate", Query.Direction.DESCENDING)
                .orderBy("pictureId", Query.Direction.DESCENDING)
                .get()
                .await()

            val picturesList: List<PictureModel> = querySnapshot.documents.mapNotNull {
                CoroutineScope(Dispatchers.IO).async {
                    PictureModel.fromHashMap(it.data as HashMap<String, Any>)
                }
            }.awaitAll()

            Resource.Success(picturesList)
        }
    }

    override suspend fun deleteAllMediaDate(userId: String): Resource<Boolean> {
        if (auth.isUserInvalid()) {
            return Resource.Error(CurrentUserIsInvalidException())
        }

        val allPictures = getProfilePictures(userId)

        return safeNetworkCall {
            val userId = auth.currentUser!!.uid
            // Delete picture from Storage
            storage
                .reference
                .child("${MediaStorageConstant.PICTURES}/$userId")
                .delete()
                .await()

            // Delete picture data from Firestore
            allPictures.data?.map { picture ->
                CoroutineScope(Dispatchers.IO).async {
                    val result = firestore
                        .collection(MediaStorageConstant.PICTURES)
                        .document(picture.pictureId!!)
                        .delete()
                        .await()

                    result
                }
            }?.awaitAll()

            Resource.Success(true)
        }
    }
}
