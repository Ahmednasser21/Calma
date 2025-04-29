package com.metafortech.calma.data.repository

import com.metafortech.calma.data.remote.presentation.login.LoginBody
import com.metafortech.calma.data.remote.presentation.login.LoginResponse
import com.metafortech.calma.data.remote.presentation.register.RegisterBody
import com.metafortech.calma.data.remote.presentation.register.RegisterResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AuthRepository {

    fun postLoginRequest(loginBody: LoginBody): Flow<Response<LoginResponse>>
    fun postRegisterRequest(registerBody: RegisterBody): Flow<Response<RegisterResponse>>
}