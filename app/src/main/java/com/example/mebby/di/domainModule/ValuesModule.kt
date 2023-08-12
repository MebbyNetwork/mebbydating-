package com.example.mebby.di.domainModule

import com.example.domain.interfaces.ValuesRepository
import com.example.domain.useCases.valuesUseCases.GetCitiesUseCase
import com.example.domain.useCases.valuesUseCases.GetInterestUseCase
import com.example.mebby.di.DomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ValuesModule : DomainModule() {
    @Provides
    @ViewModelScoped
    fun provideGetCitiesUseCase(repository: ValuesRepository): GetCitiesUseCase =
        GetCitiesUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideGetInterestUseCase(repository: ValuesRepository): GetInterestUseCase =
        GetInterestUseCase(repository)
}