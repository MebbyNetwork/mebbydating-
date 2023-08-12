package com.example.mebby.di.domainModule

import com.example.domain.interfaces.MediaRepository
import com.example.domain.useCases.mediaUseCases.AddPictureUseCase
import com.example.mebby.di.DomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class MediaModule : DomainModule() {
    @Provides
    @ViewModelScoped
    fun addPictureUseCase(repository: MediaRepository): AddPictureUseCase =
        AddPictureUseCase(repository)
}