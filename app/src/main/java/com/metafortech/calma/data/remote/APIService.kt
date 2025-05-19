package com.metafortech.calma.data.remote

import com.metafortech.calma.data.remote.interest.InterestsResponse
import com.metafortech.calma.data.remote.interest.InterestsUpdateRequest
import com.metafortech.calma.data.remote.login.LoginBody
import com.metafortech.calma.data.remote.login.LoginResponse
import com.metafortech.calma.data.remote.register.RegisterBody
import com.metafortech.calma.data.remote.register.RegisterResponse
import com.metafortech.calma.data.remote.sports.SportsResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface APIService {
    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): Response<LoginResponse>

    @GET("sports/list")
    suspend fun getSports(): Response<SportsResponse>

    @GET("interset/list")
    suspend fun getInterest(): Response<InterestsResponse>

    @POST("register")
    suspend fun register(
        @Body registerBody: RegisterBody
    ): Response<RegisterResponse>

    @POST("user/intersets/edit")
    suspend fun updateInterestsAndSports(
        @Body request: InterestsUpdateRequest,
        @Header("Authorization") token: String
    ): Response<SportsResponse>


}