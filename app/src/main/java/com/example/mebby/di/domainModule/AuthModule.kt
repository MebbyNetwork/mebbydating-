package com.example.mebby.di.domainModule

import com.example.domain.interfaces.AuthRepository
import com.example.domain.useCases.authUseCases.*
import com.example.mebby.di.DomainModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class AuthModule : DomainModule() {
    @Provides
    @ViewModelScoped
    fun provideCheckAuthUseCase(repository: AuthRepository): CheckAuthUseCase =
        CheckAuthUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSignOutUseCase(repository: AuthRepository): SignOutUseCase =
        SignOutUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSendCodeUseCase(repository: AuthRepository): SendCodeUseCase =
        SendCodeUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideSignInWithPhoneAuthCredentialUseCase(repository: AuthRepository): SignInWithPhoneAuthCredentialUseCase =
        SignInWithPhoneAuthCredentialUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideGetCredentialForPhoneAuth(repository: AuthRepository): GetCredentialForPhoneAuthUseCase =
        GetCredentialForPhoneAuthUseCase(repository)

    @Provides
    @ViewModelScoped
    fun provideResendCodeUseCase(repository: AuthRepository): ResendCodeUseCase =
        ResendCodeUseCase(repository)
}