package com.example.data.storages

import android.util.Log
import com.example.data.interfaces.GeoDBApi
import com.example.data.interfaces.ValueStorage
import com.example.data.utils.safeNetworkCall
import com.example.domain.Resource
import com.example.domain.models.InterestModel
import com.example.domain.models.city.CityModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ValueStorageImpl @Inject constructor(
    private val geoDB: GeoDBApi,
    private val realtimeDatabase: FirebaseDatabase
): ValueStorage {
    override suspend fun getCities(): Resource<List<CityModel>> {
        return withContext(Dispatchers.IO) {
            val result = geoDB.getCities()
            result.body()?.data?.let {
                return@withContext Resource.Success(it)
            } ?: Resource.Error(Exception(result.errorBody().toString()))
        }
    }

    override suspend fun getInterest(): Resource<List<InterestModel>> {
        return withContext(Dispatchers.IO) {
            return@withContext safeNetworkCall {
                val reference = realtimeDatabase.reference.child("interests/languages/eu/")

                val result = suspendCoroutine<DataSnapshot> { continuation ->
                    reference
                        .get()
                        .addOnSuccessListener { data ->
                            continuation.resume(data)
                        }
                        .addOnFailureListener {
                            continuation.resumeWithException(it)
                        }
                }



                val interests = result.children.map {
                    val result = InterestModel(
                        interestId = it.child("interestId").value.toString().toLong(),
                        interestValue = it.child("interestValue").value.toString(),
                    )

                    Log.d("interest", "$result")

                    result
                }

                Resource.Success(interests)
            }
        }
    }
}