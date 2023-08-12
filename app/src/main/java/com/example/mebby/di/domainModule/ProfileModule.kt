package com.example.mebby.di.domainModule

import com.example.domain.interfaces.ProfileRepository
import com.example.domain.useCases.profileUseCases.CreateProfileUseCase
import com.example.domain.useCases.profileUseCases.EditProfileUseCase
import com.example.domain.useCases.profileUseCases.ViewProfileUseCase
import com.example.mebby.di.DomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ProfileModule : DomainModule() {
    @Provides
    @ViewModelScoped
    fun provideCreateUserUseCase(repository: ProfileRepository): CreateProfileUseCase =
        CreateProfileUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideEditProfileUseCase(repository: ProfileRepository): EditProfileUseCase =
        EditProfileUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideViewProfileUseCase(repository: ProfileRepository): ViewProfileUseCase =
        ViewProfileUseCase(repository)
}