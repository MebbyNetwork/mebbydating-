package com.example.mebby.extensions

import com.example.mebby.data.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun DocumentReference.toFlow(): Flow<Resource<DocumentSnapshot>> = callbackFlow {
    val listener = addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            trySend(Resource.Error(exception.message ?: "something went wrong"))
            return@addSnapshotListener
        }

        if (snapshot != null) {
            trySend(Resource.Success(snapshot))
        }
    }

    awaitClose { listener.remove() }
}

fun CollectionReference.toFlow(): Flow<Resource<QuerySnapshot>> = callbackFlow {
    val listener = addSnapshotListener { snapshot, exception ->
        if (exception != null) {
            trySend(Resource.Error(exception.message ?: "something went wrong"))
            return@addSnapshotListener
        }

        if (snapshot != null) {
            trySend(Resource.Success(snapshot))
        }
    }

    awaitClose { listener.remove() }
}