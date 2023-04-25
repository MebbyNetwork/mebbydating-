package com.example.mebby.data.repositories

import android.util.Log
import com.example.mebby.const.EU_PATH
import com.example.mebby.const.INTERESTS_PATH
import com.example.mebby.const.LANGUAGES_PATH
import com.example.mebby.data.Resource
import com.example.mebby.domain.interfaces.RealtimeDatabaseRepository
import com.example.mebby.domain.models.InterestModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RealtimeDatabaseRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase,
) : RealtimeDatabaseRepository {
    override suspend fun getInterestsList(): Resource<List<InterestModel>> =
        withContext(Dispatchers.IO) {
            try {
                val reference = database.reference.child("$INTERESTS_PATH/$LANGUAGES_PATH/$EU_PATH")

                val result = suspendCoroutine<DataSnapshot> { continuation ->
                    reference.get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(task.result)
                        } else {
                            continuation.resumeWithException(task.exception ?: RuntimeException("Something went wrong"))
                        }
                    }
                }

                Log.d("getInterestsList", "$result")

                val interests = result.children.map {
                    InterestModel(
                        value = it.child("value").value.toString(),
                        key = it.child("key").value.toString().toLong(),
                        label = it.key.toString()
                    )
                }

                Resource.Success(interests)
            } catch (e: Exception) {
                Resource.Error(message = e.localizedMessage ?: "Something went wrong")
            }
        }
}