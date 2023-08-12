package com.example.data.repositories

import android.util.Log
import com.example.data.constants.*
import com.example.data.exceptions.CreateProfileFailedException
import com.example.data.exceptions.CurrentUserIsInvalidException
import com.example.data.exceptions.MediaStorageFailedException
import com.example.data.exceptions.ProfileNotFoundException
import com.example.data.interfaces.MediaStorage
import com.example.data.utils.isUserInvalid
import com.example.data.utils.safeNetworkCall
import com.example.domain.Resource
import com.example.domain.interfaces.ProfileRepository
import com.example.domain.models.*
import com.example.domain.models.city.CityModel
import com.example.domain.models.swipes.AgeRange
import com.example.domain.models.swipes.SwipesFiltersModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class ProfileRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val mediaStorage: MediaStorage,
    private val firestore: FirebaseFirestore
) : ProfileRepository {
    override suspend fun createProfile(profile: ProfileModel): Flow<Resource<Boolean>> {
        return callbackFlow<Resource<Boolean>> {
            trySend(Resource.Loading())

            // Check if current user is signed in
            if (auth.isUserInvalid()) {
                trySend(Resource.Error(CurrentUserIsInvalidException()))
                close()
            }

            // Create Profile in safeNetworkCall
            trySend(
                safeNetworkCall {
                    // Upload Pictures
                    val pictures: List<PictureModel> = profile.pictures.mapIndexed { index, pictureModel ->
                        CoroutineScope(Dispatchers.IO).async {
                            mediaStorage.uploadPicture(if (index == 0) pictureModel.copy(isProfilePicture = true) else pictureModel)
                        }
                    }.awaitAll().mapNotNull { it.data }

                    val age = ((profile.birthday!! - Timestamp.now().seconds) / (1000L * 60 * 60 * 24 * 365.25)).toInt()

                    // Filters Implement
                    val filters = SwipesFiltersModel(location = profile.city, show = profile.showType, find = profile.findType, ageRange = AgeRange(age, age + 10))

                    val filtersReference = firestore.collection(FILTERS_PATH).document(auth.currentUser?.uid!!)
                    val profileReference = firestore.collection(USERS_PATH).document(auth.currentUser?.uid!!)

                    val profileHashMap = hashMapOf<String, Any?>(
                        ProfileConstant.USERNAME to profile.username,
                        ProfileConstant.ABOUT to profile.about,
                        ProfileConstant.BIRTHDAY to profile.birthday,
                        ProfileConstant.DATE_OF_REGISTRATION to com.google.firebase.Timestamp.now().seconds,
                        ProfileConstant.GENDER_TYPE to profile.genderType,
                        ProfileConstant.FIND_TYPE to profile.findType,
                        ProfileConstant.SHOW_TYPE to profile.showType,
                        ProfileConstant.PICTURE_PROFILE to pictures.find { it.isProfilePicture }?.pictureId,
                        ProfileConstant.CITY to profile.city,
                        ProfileConstant.INTERESTS to profile.interests,
                        ProfileConstant.IS_VERIFICATION to false,
                        ProfileConstant.IS_BANNED to false,
                        ProfileConstant.USER_ID to auth.currentUser?.uid
                    )

                    firestore.runBatch { batch ->
                        // Set filters for swipe
                        batch.set(filtersReference, filters, SetOptions.merge())

                        // Set profile
                        batch.set(profileReference, profileHashMap, SetOptions.merge())
                    }.await()

                    Resource.Success(true)
                }
            )
            close()

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun editProfile(profile: ProfileModel): Flow<Resource<Boolean>> {
        return callbackFlow<Resource<Boolean>> {
            trySend(Resource.Loading())

            // Check if current user is signed in
            if (auth.isUserInvalid()) {
                trySend(Resource.Error(CurrentUserIsInvalidException()))
                close()
            }

            // Create Profile in safeNetworkCall
            trySend(
                safeNetworkCall {
                    val profileHashMap = hashMapOf<String, Any?>(
                        ProfileConstant.USERNAME to profile.username,
                        ProfileConstant.ABOUT to profile.about,
                        ProfileConstant.BIRTHDAY to profile.birthday,
                        ProfileConstant.GENDER_TYPE to profile.genderType,
                        ProfileConstant.CITY to profile.city,
                        ProfileConstant.INTERESTS to profile.interests,
                        ProfileConstant.LAST_EDIT_DATE to com.google.firebase.Timestamp.now().seconds
                    )

                    firestore
                        .collection(USERS_PATH)
                        .document(auth.currentUser!!.uid)
                        .update(profileHashMap)
                        .await()

                    Resource.Success(true)
                }
            )
            close()

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun deleteProfile(): Flow<Resource<Boolean>> {
        return callbackFlow<Resource<Boolean>> {
            trySend(Resource.Loading())

            // Check if current user is signed in
            if (auth.isUserInvalid()) {
                trySend(Resource.Error(CurrentUserIsInvalidException()))
                close()
            }

            trySend(
                safeNetworkCall {
                    val response = firestore
                        .collection(USERS_PATH)
                        .document(auth.currentUser!!.uid)

                    val documentSnapshot = response
                        .get()
                        .await()

                    if (documentSnapshot.exists()) {
                        response
                            .delete()
                            .await()
                    } else {
                        return@safeNetworkCall Resource.Error(ProfileNotFoundException("Oops, profile not found."))
                    }

                    val result = mediaStorage.deleteAllMediaDate(auth.currentUser?.uid!!)

                    if (result is Resource.Success) {
                        Resource.Success(true)
                    } else {
                        Resource.Error(MediaStorageFailedException("Oops, some went wrong"))
                    }
                }
            )
            close()

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun viewProfile(userId: String?): Flow<Resource<ProfileModel>> {
        return callbackFlow<Resource<ProfileModel>> {
            trySend(Resource.Loading())

            // Check if current user is signed in
            if (auth.isUserInvalid()) {
                trySend(Resource.Error(CurrentUserIsInvalidException()))
                close()
            }

            trySend(
                safeNetworkCall {
                    val uid = userId ?: auth.currentUser!!.uid

                    val profileSnapshot = firestore
                        .collection(USERS_PATH)
                        .document(uid)
                        .get()
                        .await()

                    if (profileSnapshot.exists()) {
                        val isVerification = profileSnapshot.getBoolean(ProfileConstant.IS_VERIFICATION)
                        Log.d("checkingProfile", "isVerification")
                        val isBanned = profileSnapshot.getBoolean(ProfileConstant.IS_BANNED)
                        Log.d("checkingProfile", "isBanned")
                        val userId = profileSnapshot.getString(ProfileConstant.USER_ID)
                        Log.d("checkingProfile", "userId")
                        val username = profileSnapshot.getString(ProfileConstant.USERNAME)
                        Log.d("checkingProfile", "username")
                        val about = profileSnapshot.getString(ProfileConstant.ABOUT)
                        Log.d("checkingProfile", "about")
                        val birthday = profileSnapshot.getLong(ProfileConstant.BIRTHDAY)
                        Log.d("checkingProfile", "birthday")
                        val dateOfRegistration = profileSnapshot.getLong(ProfileConstant.DATE_OF_REGISTRATION)
                        Log.d("checkingProfile", "dateOfRegistration")
                        val city = CityModel.fromHashMap(profileSnapshot.get(ProfileConstant.CITY) as HashMap<String, Any?>)
                        Log.d("checkingProfile", "city")
                        val interests = (profileSnapshot.get(ProfileConstant.INTERESTS) as List<HashMap<String, Any?>>).map { InterestModel.fromHashMap(it) }
                        Log.d("checkingProfile", "interest")
                        val pictureProfile = PictureModel.fromHashMap(
                            firestore
                                .collection(MediaStorageConstant.PICTURES)
                                .document(profileSnapshot.get(ProfileConstant.PICTURE_PROFILE) as String)
                                .get()
                                .await()
                                .data as HashMap<String, Any>
                        )
                        Log.d("checkingProfile", "pictureProfile")
                        val pictures = mediaStorage.getProfilePictures(uid).data
                        Log.d("checkingProfile", "pictures")
                        val find = profileSnapshot.getString(ProfileConstant.FIND_TYPE)
                        Log.d("checkingProfile", "find")
                        val show = profileSnapshot.getString(ProfileConstant.SHOW_TYPE)
                        Log.d("checkingProfile", "show")
                        val gender = profileSnapshot.getString(ProfileConstant.GENDER_TYPE)
                        Log.d("checkingProfile", "gender")

                        if (isVerification != null && isBanned != null && userId != null && username != null
                            && about != null && birthday != null && dateOfRegistration != null && city != null
                            && interests != null && pictureProfile != null && find != null && show != null && gender != null) {
                            val profile = ProfileModel(
                                isVerification = isVerification,
                                isBanned = isBanned,
                                userId = userId,
                                username = username,
                                about = about,
                                birthday = birthday,
                                dateOfRegistration = dateOfRegistration,
                                city = city,
                                interests = interests,
                                pictureProfile = pictureProfile,
                                pictures = pictures ?: listOf(),
                                findType = find,
                                showType = show,
                                genderType = gender
                            )

                            Resource.Success(profile)
                        } else {
                            Resource.Error(ProfileNotFoundException("Something went wrong"))
                        }
                    } else {
                        Resource.Error(ProfileNotFoundException("Profile $userId not found"))
                    }
                }
            )
            close()

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun blockProfile(blockedUserId: String): Flow<Resource<Boolean>> {
        return callbackFlow<Resource<Boolean>> {
            trySend(Resource.Loading())

            // Check if current user is signed in
            if (auth.isUserInvalid()) {
                trySend(Resource.Error(CurrentUserIsInvalidException()))
                close()
            }

            trySend(
                safeNetworkCall {
                    val blockedProfile = BlockedProfileModel(
                        userId = auth.currentUser!!.uid,
                        blockedUserId = blockedUserId,
                        blockedUUID = UUID.randomUUID().toString(),
                        blockTime = com.google.firebase.Timestamp.now().seconds
                    )

                    firestore
                        .collection(USERS_PATH)
                        .document(auth.currentUser!!.uid)
                        .collection(BLOCKS_PATH)
                        .document(blockedUserId)
                        .set(blockedProfile)
                        .await()

                    Resource.Success(true)
                }
            )
            close()

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun pardonProfile(blockedUserId: String): Flow<Resource<Boolean>> {
        return callbackFlow<Resource<Boolean>> {
            trySend(Resource.Loading())

            // Check if current user is signed in
            if (auth.isUserInvalid()) {
                trySend(Resource.Error(CurrentUserIsInvalidException()))
                close()
            }

            trySend(
                safeNetworkCall {
                    firestore
                        .collection(USERS_PATH)
                        .document(auth.currentUser!!.uid)
                        .collection(BLOCKS_PATH)
                        .document(blockedUserId)
                        .delete()
                        .await()

                    Resource.Success(true)
                }
            )
            close()

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun reportProfile(report: ReportModel): Flow<Resource<Boolean>> {
        return callbackFlow<Resource<Boolean>> {
            trySend(Resource.Loading())

            // Check if current user is signed in
            if (auth.isUserInvalid()) {
                trySend(Resource.Error(CurrentUserIsInvalidException()))
                close()
            }

            trySend(
                safeNetworkCall {
                    val reportModel = report.copy(
                        userId = auth.currentUser!!.uid,
                        reportTime = com.google.firebase.Timestamp.now().seconds
                    )

                    firestore
                        .collection(REPORT_PATH)
                        .add(reportModel)
                        .await()

                    Resource.Success(true)
                }
            )
            close()

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }
}
