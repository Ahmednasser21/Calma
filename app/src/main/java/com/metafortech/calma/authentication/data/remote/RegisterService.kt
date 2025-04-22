package com.metafortech.calma.authentication.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterService {
    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ):LoginResponse
}