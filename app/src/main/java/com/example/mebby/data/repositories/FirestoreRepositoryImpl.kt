package com.example.mebby.data.repositories

import com.example.mebby.const.*
import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.FirestoreRepository
import com.example.mebby.domain.interfaces.StorageRepository
import com.example.mebby.domain.models.ImageModel
import com.example.mebby.domain.models.UserModel
import com.example.mebby.domain.models.UserProfileModel
import com.example.mebby.extensions.convertToSHA256
import com.example.mebby.mapper.cityMapper
import com.example.mebby.mapper.imageMapper
import com.example.mebby.mapper.interestMapper
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageRepository: StorageRepository
) : FirestoreRepository {
    private val auth = Firebase.auth

    override suspend fun createUserProfile(user: UserModel) = callbackFlow {
        val currentUser = auth.currentUser

        trySend(Resource.Loading())

        if (currentUser == null) {
            trySend(Resource.Error("Current user is null"))
            close()
            return@callbackFlow
        }

        try {
            val images = user.images
            if (images != null) {
                addImages(images)
            }

            val userHashMap = hashMapOf(
                NAME to user.name,
                BIRTHDAY to user.birthday.time,
                GENDER to user.gender.value,
                FIND to user.find.value,
                SHOW to user.show.value,
                GENERAL_IMAGE to images[0].uri,
                INTEREST to user.interest,
                ABOUT to user.about?.trim(),
                CITY to user.city,
                DATE_OF_REGISTRATION to Timestamp.now().seconds,
                PHONE_NUMBER to (currentUser.phoneNumber?.convertToSHA256() ?: "None"),
                VERIFICATION to false
            )

            val reference = firestore.collection(USERS_CHILD).document(currentUser.uid)

            val result = reference.get().await()
            if (!result.exists()) {
                reference.set(userHashMap).await()
                trySend(Resource.Success(true))
                close()
            } else {
                trySend(Resource.Error("User already exists"))
                close()
            }
        } catch (e: Exception) {
        trySend(Resource.Error(e.localizedMessage ?: "Something went wrong"))
        close()
    }
    awaitClose()
}.flowOn(Dispatchers.IO)

override suspend fun getUserProfile(uid: String?) = callbackFlow<Resource<UserProfileModel>> {
    val currentUser = auth.currentUser

    trySend(Resource.Loading())

    if (currentUser == null) {
        trySend(Resource.Error(message = "Current User is null"))
        close()
        return@callbackFlow
    }

    val userId = uid ?: currentUser.uid

    val userDocumentRef = firestore.collection(USERS_CHILD).document(userId)

    val listenerRegistration = userDocumentRef.addSnapshotListener { snapshot, e ->
        if (e != null) {
            trySend(Resource.Error(e.localizedMessage ?: "something went wrong"))
            close()
        }

        if (snapshot?.exists() == true || snapshot != null) {
            val data = snapshot.data!!

            val interests = data[INTEREST] as List<MutableMap<String, Any>>
            val city = data[CITY] as MutableMap<String, Any>

            CoroutineScope(Dispatchers.IO).launch {
                val images = async {
                    val imagesSnapshot = firestore
                        .collection("media")
                        .document(currentUser.uid)
                        .collection("images")
                        .get()
                        .await()
                    return@async imagesSnapshot.documents.map { it.data }
                }

                val user = UserProfileModel(
                    KYC = data[VERIFICATION] as Boolean,
                    about = data[ABOUT] as String,
                    birthday = data[BIRTHDAY] as Long,
                    city = city.cityMapper(),
                    dateOfRegistration = data[DATE_OF_REGISTRATION] as Long,
                    find = data[FIND] as String,
                    gender = data[GENDER] as String,
                    interests = interests.map { it.interestMapper() },
                    phoneNumber = data[PHONE_NUMBER] as String,
                    generalImage = data[GENERAL_IMAGE] as String,
                    profileImage = (images.await().map { it?.imageMapper() }
                        ?: null) as List<ImageModel>?,
                    show = data[SHOW] as String,
                    username = data[NAME] as String
                )

                trySend(Resource.Success(user))
                close()
            }
        } else {
            trySend(Resource.Error("User not found"))
            close()
        }
    }

    awaitClose { listenerRegistration.remove() }
}.flowOn(Dispatchers.IO)

override suspend fun changeUserProfile(user: UserProfileModel) = flow<Resource<Boolean>> {
    emit(Resource.Loading())

    val currentUser = auth.currentUser
    if (currentUser == null) {
        emit(Resource.Error("Current user is null"))
        return@flow
    }

    val userProfileUpdates = hashMapOf(
        NAME to user.username,
        BIRTHDAY to user.birthday,
        GENDER to user.gender,
        FIND to user.find,
        SHOW to user.show,
        GENERAL_IMAGE to user.generalImage,
        INTEREST to user.interests,
        ABOUT to user.about?.trim(),
        CITY to user.city
    )

    try {
        val userDocumentRef = firestore.collection(USERS_CHILD).document(currentUser.uid)
        val userDocumentSnapshot = userDocumentRef.get().await()
        if (userDocumentSnapshot.exists()) {
            userDocumentRef.update(userProfileUpdates).await()
            emit(Resource.Success(true))
        } else {
            emit(Resource.Error("User not found"))
        }

    } catch (e: Exception) {
        emit(Resource.Error(e.localizedMessage ?: "Something went wrong"))
    }

}.flowOn(Dispatchers.IO)

override suspend fun addImages(images: List<ImageModel>) = callbackFlow {
    val images = storageRepository.addImages(images).data ?: return@callbackFlow
    val uid = auth.currentUser?.uid

    if (uid == null) {
        trySend(Resource.Error("uid is null"))
        close()
        return@callbackFlow
    }

    trySend(Resource.Loading())

    CoroutineScope(Dispatchers.IO).launch {
        try {
            for (image in images) {
                val imageReference = firestore
                    .collection("media")
                    .document(uid)
                    .collection("images")
                    .document(image.uri.convertToSHA256())

                imageReference.set(image).await()
            }

            trySend(Resource.Success(true))
            close()
        } catch (e: Exception) {
            trySend(Resource.Error(e.localizedMessage ?: "Something went wrong"))
            close()
        }
    }

    awaitClose()
}

companion object {
    const val TAG = "DatabaseRepositoryImpl"
}
}
