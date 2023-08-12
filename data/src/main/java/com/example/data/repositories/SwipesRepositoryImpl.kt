package com.example.data.repositories

import com.example.data.constants.FILTERS_PATH
import com.example.data.constants.LIKED_USERS
import com.example.data.constants.USERS_PATH
import com.example.data.exceptions.CurrentUserIsInvalidException
import com.example.data.utils.safeNetworkCall
import com.example.domain.Resource
import com.example.domain.interfaces.SwipesRepository
import com.example.domain.models.PictureModel
import com.example.domain.models.city.CityModel
import com.example.domain.models.swipes.CardModel
import com.example.domain.models.swipes.SwipesFiltersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class SwipesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : SwipesRepository {
    override suspend fun getSwipeCards(filters: SwipesFiltersModel?): Flow<Resource<List<CardModel>>> {
        return callbackFlow {
            trySend(Resource.Loading())

            val city = CityModel(
                city = "Moscow",
                country = "Russia",
                countryCode = "RU",
                id = 3350606,
                latitude = 42.46245,
                longitude = 1.50905,
                name = "Moscow",
                population = 10000423,
                region = "Central Region",
                regionCode = "05",
                type = "CITY",
                wikiDataId = "Q43243"
            )

            val data = listOf(
                CardModel(
                    username = "Lily",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1610642434250-392436bd9fba?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=765&q=80"),
                    city = city,
                    age = 22,
                    about = "The quick brown fox jumps over the lazy dog.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Sophia",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1592206934769-67dc0e88b5e3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=726&q=80"),
                    city = city,
                    age = 23,
                    about = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Ava",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1682723840187-313a08133fcb?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=764&q=80"),
                    city = city,
                    age = 25,
                    about = "The sky was a deep shade of blue, and the clouds were fluffy and white.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Isabela",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://plus.unsplash.com/premium_photo-1664442593274-38caa77af985?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"),
                    city = city,
                    age = 20,
                    about = "She walked down the street, lost in thought.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Mia",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1682783432407-75b14dd288d3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"),
                    city = city,
                    age = 19,
                    about = "The ocean waves crashed against the shore, creating a calming rhythm.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Emily",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1682615240935-333606d5643a?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=688&q=80"),
                    city = city,
                    age = 25,
                    about = "The sound of laughter filled the room, and everyone felt happy.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Harper",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1682530778470-4025e3c52011?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"),
                    city = city,
                    age = 23,
                    about = "He looked up at the stars, marveling at the vastness of the universe.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Emma",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1592621385612-4d7129426394?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"),
                    city = city,
                    age = 21,
                    about = "The smell of freshly baked bread wafted through the air, making everyone hungry.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Chloe",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1488716820095-cbe80883c496?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=686&q=80"),
                    city = city,
                    age = 24,
                    about = "She closed her eyes and took a deep breath, feeling the tension leave her body.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Avery",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1514315384763-ba401779410f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=683&q=80"),
                    city = city,
                    age = 25,
                    about = "The leaves rustled in the wind, creating a soothing sound.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),

                CardModel(
                    username = "Victoria",
                    isVerification = true,
                    picture = PictureModel(imageUrl = "https://images.unsplash.com/photo-1597586124394-fbd6ef244026?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80"),
                    city = city,
                    age = 26,
                    about = "The sun slowly set over the horizon, casting a warm glow over the peaceful countryside. Birds chirped and sang their evening songs, bidding farewell to the day. As the sky turned from blue to pink to orange, the world seemed to slow down and breathe a collective sigh of contentment. It was a moment of pure beauty, and for a brief instant, all worries and troubles were forgotten.",
                    userId = "EsOfB2tT5FWOjjBaKHWCoeKU9Ok1"
                ),
            )

            trySend(Resource.Success(data))
            close()

            awaitClose()
        }
    }

    override suspend fun getSwipeFilters(): Flow<Resource<SwipesFiltersModel>> {
        return callbackFlow {
            trySend(Resource.Loading())

            trySend(
                safeNetworkCall {
                    val result = firestore.collection(FILTERS_PATH).document(auth.currentUser?.uid!!).get().await().data
                    val filters = SwipesFiltersModel.fromHashMap(result as HashMap<String, Any?>)

                    Resource.Success(filters)
                }
            )
            close()

            awaitClose()
        }
    }

    override suspend fun updateSwipeFilters(filters: SwipesFiltersModel): Flow<Resource<Boolean>> {
        return callbackFlow {
            trySend(Resource.Loading())

            trySend(
                safeNetworkCall {
                    firestore.collection(FILTERS_PATH).document(auth.currentUser?.uid!!).update(filters.toHashMap()).await()

                    Resource.Success(true)
                }
            )
            close()

            awaitClose()
        }
    }

    override suspend fun likeUser(userId: String): Resource<Boolean> {
        val currentUser = auth.currentUser ?: return Resource.Error(CurrentUserIsInvalidException())

        return safeNetworkCall {
            val userLikedReference = firestore
                .collection(USERS_PATH)
                .document(currentUser.uid)
                .collection(LIKED_USERS)
                .document(userId)

            val userLikedSnapshot = userLikedReference.get().await()

            if (userLikedSnapshot.exists()) {
                return Resource.Success(true)
            }

            val userReference = firestore.collection(USERS_PATH).document(userId)
            val userSnapshot = userReference.get().await()
            if (!userSnapshot.exists()) {
                return Resource.Error(CurrentUserIsInvalidException())
            }

            val batch = firestore.batch()
            batch.set(userLikedReference, mapOf(
                "liked" to true,
                "timestamp" to FieldValue.serverTimestamp()
            ))
            batch.update(userReference, "likesCount", FieldValue.increment(1))
            batch.commit().await()

            return Resource.Success(true)
        }
    }

    override suspend fun passUser(userId: String): Resource<Boolean> {
        val currentUser = auth.currentUser ?: return Resource.Error(CurrentUserIsInvalidException())

        return safeNetworkCall {
            val userLikedReference = firestore
                .collection(USERS_PATH)
                .document(currentUser.uid)
                .collection(LIKED_USERS)
                .document(userId)

            val userLikedSnapshot = userLikedReference.get().await()

            if (userLikedSnapshot.exists()) {
                return Resource.Success(true)
            }

            val userReference = firestore.collection(USERS_PATH).document(userId)
            val userSnapshot = userReference.get().await()
            if (!userSnapshot.exists()) {
                return Resource.Error(CurrentUserIsInvalidException())
            }

            val batch = firestore.batch()
            batch.set(userLikedReference, mapOf("liked" to true))
            batch.update(userReference, "likesCount", FieldValue.increment(1))
            batch.commit().await()

            return Resource.Success(true)
        }
    }

    override suspend fun undoSwipe(userId: String): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getMatchedProfilesUseCase(): Flow<Resource<String>> {
        TODO("Not yet implemented")
    }
}