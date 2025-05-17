package com.metafortech.calma.di

import com.metafortech.calma.data.repository.AuthRepository
import com.metafortech.calma.data.repository.AuthRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImp:AuthRepositoryImp
    ): AuthRepository

}