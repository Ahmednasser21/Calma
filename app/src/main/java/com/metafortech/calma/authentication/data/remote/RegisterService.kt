package com.metafortech.calma.authentication.data.remote

import com.metafortech.calma.authentication.data.remote.login.LoginBody
import com.metafortech.calma.authentication.data.remote.login.LoginResponse
import com.metafortech.calma.authentication.data.remote.register.RegisterBody
import com.metafortech.calma.authentication.data.remote.register.RegisterResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): Response<LoginResponse>

    @POST("register")
    suspend fun register(
        @Body registerBody: RegisterBody
    ): Response<RegisterResponse>

}