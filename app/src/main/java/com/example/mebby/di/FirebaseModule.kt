package com.example.mebby.di

import com.example.mebby.const.DATABASE_REFERENCE
import com.example.mebby.const.STORAGE_REFERENCE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideRealtimeDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(DATABASE_REFERENCE)

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage = Firebase.storage(STORAGE_REFERENCE)

    @Provides
    @Singleton
    fun provideAuth(): FirebaseAuth = Firebase.auth
}
