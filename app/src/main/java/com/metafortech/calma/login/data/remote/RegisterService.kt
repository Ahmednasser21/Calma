package com.metafortech.calma.login.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ):LoginResponse
}