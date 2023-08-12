package com.example.mebby.di

import android.content.Context
import com.example.data.interfaces.GeoDBApi
import com.example.data.interfaces.MediaStorage
import com.example.data.interfaces.ValueStorage
import com.example.data.repositories.*
import com.example.data.storages.MediaStorageImpl
import com.example.data.storages.ValueStorageImpl
import com.example.domain.interfaces.*
import com.example.mebby.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideAuthRepository(firestore: FirebaseFirestore, activity: MainActivity, auth: FirebaseAuth): AuthRepository =
        AuthRepositoryImpl(firestore, activity = activity, auth = auth)

    @Provides
    @Singleton
    fun provideProfileRepository(auth: FirebaseAuth, mediaStorage: MediaStorage, firestore: FirebaseFirestore): ProfileRepository =
        ProfileRepositoryImpl(auth, mediaStorage, firestore)

    @Provides
    @Singleton
    fun provideValuesRepository(storage: ValueStorage): ValuesRepository =
        ValuesRepositoryImpl(storage)

    @Provides
    @Singleton
    fun provideMediaRepository(mediaStorage: MediaStorage, auth: FirebaseAuth): MediaRepository =
        MediaRepositoryImpl(mediaStorage, auth)

    @Provides
    @Singleton
    fun provideMediaStorage(firestore: FirebaseFirestore, storage: FirebaseStorage, auth: FirebaseAuth, @ApplicationContext context: Context): MediaStorage =
        MediaStorageImpl(firestore, storage, auth, context = context)

    @Provides
    @Singleton
    fun provideValuesStorage(geoDBApi: GeoDBApi, database: FirebaseDatabase): ValueStorage =
        ValueStorageImpl(geoDB = geoDBApi, database)

    @Provides
    @Singleton
    fun provideSwipesRepository(firestore: FirebaseFirestore, auth: FirebaseAuth): SwipesRepository =
        SwipesRepositoryImpl(firestore, auth)
}