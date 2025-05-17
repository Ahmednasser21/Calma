package com.metafortech.calma.di

import com.metafortech.calma.domain.google.GoogleSignInUseCase
import com.metafortech.calma.domain.google.GoogleSignInUseCaseImpl
import com.metafortech.calma.domain.interest.InterestUseCase
import com.metafortech.calma.domain.interest.InterestUseCaseImpl
import com.metafortech.calma.domain.login.LoginUseCase
import com.metafortech.calma.domain.login.LoginUseCaseImpl
import com.metafortech.calma.domain.register.RegisterUseCase
import com.metafortech.calma.domain.register.RegisterUseCaseImpl
import com.metafortech.calma.domain.sports.SportUseCase
import com.metafortech.calma.domain.sports.SportUseCaseImp
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

    @Binds
    abstract fun bindInterestUseCase(interestUseCaseImp: InterestUseCaseImpl): InterestUseCase

    @Binds
    abstract fun bindSportUseCase(sportUseCaseImp: SportUseCaseImp): SportUseCase
}