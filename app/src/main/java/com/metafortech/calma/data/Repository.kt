package com.metafortech.calma.data

import com.metafortech.calma.data.remote.login.LoginResponse
import com.metafortech.calma.data.remote.RegisterService
import com.metafortech.calma.data.remote.login.LoginBody
import com.metafortech.calma.data.remote.register.RegisterBody
import com.metafortech.calma.data.remote.register.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val registerService: RegisterService
) {
    fun postLoginRequest(loginBody: LoginBody): Flow <Response<LoginResponse>> = flow {
        emit(registerService.login(loginBody))
    }

    fun postRegisterRequest(registerBody: RegisterBody): Flow<Response<RegisterResponse>> = flow {
        emit(registerService.register(registerBody))
    }
}