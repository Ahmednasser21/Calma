package com.metafortech.calma.data.repository

import com.metafortech.calma.data.remote.interest.InterestsResponse
import com.metafortech.calma.data.remote.login.LoginBody
import com.metafortech.calma.data.remote.login.LoginResponse
import com.metafortech.calma.data.remote.register.RegisterBody
import com.metafortech.calma.data.remote.register.RegisterResponse
import com.metafortech.calma.data.remote.sports.SportsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AuthRepository {

    fun postLoginRequest(loginBody: LoginBody): Flow<Response<LoginResponse>>
    fun getInterest(): Flow<Response<InterestsResponse>>
    fun getSports(): Flow<Response<SportsResponse>>
    fun postRegisterRequest(registerBody: RegisterBody): Flow<Response<RegisterResponse>>
}