package com.example.mebby.di.domainModule

import com.example.domain.interfaces.SwipesRepository
import com.example.domain.useCases.swipesUseCases.*
import com.example.mebby.di.DomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class SwipeModule : DomainModule() {
    @Provides
    @ViewModelScoped
    fun provideGetUsersCardUseCae(repository: SwipesRepository): GetSwipeCardsUseCase =
        GetSwipeCardsUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSwipeLikeUseCase(repository: SwipesRepository): SwipeLikeUseCase =
        SwipeLikeUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSwipePassUseCase(repository: SwipesRepository): SwipePassUseCase =
        SwipePassUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideGetSwipeFiltersUseCase(repository: SwipesRepository): GetSwipeFiltersUseCase =
        GetSwipeFiltersUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideUpdateSwipeFiltersUseCase(repository: SwipesRepository): UpdateSwipeFiltersUseCase =
        UpdateSwipeFiltersUseCase(repository)
}