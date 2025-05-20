package com.metafortech.calma.di

import com.metafortech.calma.data.local.AppPreferences
import com.metafortech.calma.data.local.AppPreferencesImpl
import com.metafortech.calma.data.repository.AuthRepository
import com.metafortech.calma.data.repository.AuthRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImp:AuthRepositoryImp
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindAppPreferences(
        appPreferencesImpl: AppPreferencesImpl
    ): AppPreferences

}