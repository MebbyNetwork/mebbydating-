package com.example.mebby.di

import com.example.mebby.domain.interfaces.*
import com.example.mebby.domain.useCases.authUseCases.*
import com.example.mebby.domain.useCases.mediaUseCases.AddImageUseCase
import com.example.mebby.domain.useCases.mediaUseCases.DeleteImageUseCase
import com.example.mebby.domain.useCases.mediaUseCases.UploadImageUseCase
import com.example.mebby.domain.useCases.mediaUseCases.UploadImagesUseCase
import com.example.mebby.domain.useCases.profileUseCases.EditProfileUseCase
import com.example.mebby.domain.useCases.profileUseCases.CreateProfileUseCase
import com.example.mebby.domain.useCases.profileUseCases.GetProfileUseCase
import com.example.mebby.domain.useCases.sDataUseCases.GetCitiesListUseCase
import com.example.mebby.domain.useCases.sDataUseCases.GetInterestsListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {
    @Provides
    @ViewModelScoped
    fun provideGetCitiesListUseCase(repository: ApiRepository): GetCitiesListUseCase =
        GetCitiesListUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideGetInterestsListUseCase(repository: RealtimeDatabaseRepository): GetInterestsListUseCase =
        GetInterestsListUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSignInWithPhoneAuthCredentialUseCase(repository: AuthRepository): SignInWithPhoneAuthCredentialUseCase =
        SignInWithPhoneAuthCredentialUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSendVerificationCodeUseCase(repository: AuthRepository): SendVerificationCodeUseCase =
        SendVerificationCodeUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideResendCodeSignInUseCase(repository: AuthRepository): ResendCodeSignInUseCase =
        ResendCodeSignInUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideGetCredentialPhoneAuthUseCase(repository: AuthRepository): GetCredentialForPhoneAuthUseCase =
        GetCredentialForPhoneAuthUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideCreateUserUseCase(repository: FirestoreRepository): CreateProfileUseCase =
        CreateProfileUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideUploadImageUseCase(repository: StorageRepository): UploadImageUseCase =
        UploadImageUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideDeleteImageUseCase(repository: StorageRepository): DeleteImageUseCase =
        DeleteImageUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideAddImagesToUseCase(repository: StorageRepository): UploadImagesUseCase =
        UploadImagesUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideCheckAuthUseCase(repository: AuthRepository): CheckAuthUseCase =
        CheckAuthUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideGetUserUseCase(repository: FirestoreRepository): GetProfileUseCase =
        GetProfileUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSignOutUseCase(repository: AuthRepository): SignOutUseCase =
        SignOutUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideChangeUserProfileUseCase(repository: FirestoreRepository): EditProfileUseCase =
        EditProfileUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideAddImageUseCase(repository: FirestoreRepository) =
        AddImageUseCase(repository)
}