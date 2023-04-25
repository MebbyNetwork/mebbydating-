package com.example.mebby.di

import com.example.mebby.data.network.interfaces.CityApi
import com.example.mebby.data.repositories.*
import com.example.mebby.domain.interfaces.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideApiRepository(cityApi: CityApi): ApiRepository =
        ApiRepositoryImpl(cityApi)

    @Provides
    @Singleton
    fun provideFirestoreRepository(
        firestore: FirebaseFirestore,
        storageRepository: StorageRepository
        ): FirestoreRepository =
        FirestoreRepositoryImpl(firestore, storageRepository)

    @Provides
    @Singleton
    fun provideAuthRepository(firestore: FirebaseFirestore): AuthRepository =
        AuthRepositoryImpl(firestore)
    @Provides
    @Singleton
    fun provideStorageRepository(storage: FirebaseStorage): StorageRepository =
        StorageRepositoryImpl(storage)

    @Provides
    @Singleton
    fun provideRealtimeDatabaseRepository(database: FirebaseDatabase): RealtimeDatabaseRepository =
        RealtimeDatabaseRepositoryImpl(database)
}