package com.metafortech.calma.data.remote

import com.metafortech.calma.data.remote.presentation.login.LoginBody
import com.metafortech.calma.data.remote.presentation.login.LoginResponse
import com.metafortech.calma.data.remote.presentation.register.RegisterBody
import com.metafortech.calma.data.remote.presentation.register.RegisterResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.POST

interface APIService {
    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body registerBody: RegisterBody
    ): Response<RegisterResponse>

}