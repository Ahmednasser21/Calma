package com.metafortech.calma.authentication.data.di

import com.metafortech.calma.authentication.data.remote.RegisterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RegisterDataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://helal.jonior.net/calma/public/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideRegisterService(
        retrofit: Retrofit
    ): RegisterService {
        return retrofit.create(RegisterService::class.java)
    }
}