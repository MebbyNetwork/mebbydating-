package com.example.mebby.data.repositories

import com.example.mebby.const.*
import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.ProfileRepository
import com.example.mebby.domain.models.change.ProfileModel
import com.example.mebby.extensions.convertToSHA256
import com.example.mebby.util.safeNetworkCall
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProfileRepository {
    private val auth = Firebase.auth

    // Create Profile
    override fun createProfile(userProfile: ProfileModel): Flow<Resource<Boolean>> {
        return callbackFlow {
            val currentUser = auth.currentUser
            trySend(Resource.Loading())

            if (currentUser == null) {
                trySend(Resource.Error("Current user is null"))
                close()
                return@callbackFlow
            }

            val safeCall = safeNetworkCall {
                val userProfileHashMap = hashMapOf<String, Any>()

                val pictures = userProfile.pictures
//                addImages(images)

                with(userProfile) {
                    userProfileHashMap[ProfileConstants.USERNAME] = this.username
                    userProfileHashMap[ProfileConstants.ABOUT] = this.about
                    userProfileHashMap[ProfileConstants.BIRTHDAY] = this.birthday
                    userProfileHashMap[ProfileConstants.GENDER_TYPE] = this.genderType
                    userProfileHashMap[ProfileConstants.FIND_TYPE] = this.findType
                    userProfileHashMap[ProfileConstants.SHOW_TYPE] = this.showType
                    userProfileHashMap[ProfileConstants.PICTURE_PROFILE] = this.pictureProfile
                    userProfileHashMap[ProfileConstants.PICTURES] = this.pictures
                    userProfileHashMap[ProfileConstants.CITY] = this.city
                    userProfileHashMap[ProfileConstants.INTERESTS] = this.interests
                    userProfileHashMap[ProfileConstants.IS_VERIFICATION] = false
                    userProfileHashMap[ProfileConstants.DATE_OF_REGISTRATION] = Timestamp.now().seconds
                    userProfileHashMap[ProfileConstants.PHONE_NUMBER_HASH] = currentUser.phoneNumber?.convertToSHA256() ?: NONE
                }

                val reference = firestore.collection(USERS_CHILD).document(currentUser.uid)

                val result = suspendCoroutine<Resource<Boolean>> { continuation ->
                    reference.addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            continuation.resume(Resource.Error(exception.message ?: SWW))
                        }

                        if (snapshot?.exists() == false) {
                            reference.set(userProfileHashMap)
                            continuation.resume(Resource.Success(true))
                        }
                    }
                }

                return@safeNetworkCall result
            }

            trySend(safeCall)
            close()

            awaitClose()
        }
    }

    override fun editProfile(userId: String, userProfile: ProfileModel): Flow<Resource<Boolean>> {
        return callbackFlow {

        }
    }

    override fun verifyProfile(
        userId: String,
        verificationType: String,
        verificationData: String,
    ): Flow<Resource<Boolean>> {
        return callbackFlow {

        }
    }

    override fun deactivateProfile(userId: String): Flow<Resource<Boolean>> {
        return callbackFlow {

        }
    }

    override fun removeProfile(userId: String): Flow<Resource<Boolean>> {
        return callbackFlow {

        }
    }

    override fun viewProfile(userId: String, viewedUserId: String): Flow<Resource<ProfileModel>> {
        return callbackFlow {

        }
    }

    override fun blockProfile(userId: String, blockedUserId: String): Flow<Resource<Boolean>> {
        return callbackFlow {

        }
    }

    override fun unblockProfile(userId: String, blockedUserId: String): Flow<Resource<Boolean>> {
        return callbackFlow {

        }
    }

    override fun reportProfile(
        userId: String,
        reportedUserId: String,
        reportReason: String,
    ): Flow<Resource<Boolean>> {
        return callbackFlow {

        }
    }
}