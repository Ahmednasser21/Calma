package com.metafortech.calma.presentation.authentication.di

import com.metafortech.calma.presentation.authentication.google.GoogleSignInUseCase
import com.metafortech.calma.presentation.authentication.google.GoogleSignInUseCaseImpl
import com.metafortech.calma.presentation.authentication.login.domain.LoginUseCase
import com.metafortech.calma.presentation.authentication.login.domain.LoginUseCaseImpl
import com.metafortech.calma.presentation.authentication.register.domain.RegisterUseCase
import com.metafortech.calma.presentation.authentication.register.domain.RegisterUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCasesModule {

    @Binds
    abstract fun bindLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase
    @Binds
    abstract fun bindRegisterUseCase(registerUseCaseImpl: RegisterUseCaseImpl): RegisterUseCase
    @Binds
    abstract fun bindGoogleSignInUseCase(
        googleSignInUseCaseImpl: GoogleSignInUseCaseImpl
    ): GoogleSignInUseCase

}