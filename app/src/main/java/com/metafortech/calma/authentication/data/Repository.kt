package com.metafortech.calma.authentication.data

import com.metafortech.calma.authentication.data.remote.login.LoginResponse
import com.metafortech.calma.authentication.data.remote.RegisterService
import com.metafortech.calma.authentication.data.remote.login.LoginBody
import com.metafortech.calma.authentication.data.remote.register.RegisterBody
import com.metafortech.calma.authentication.data.remote.register.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val registerService: RegisterService
) {
    fun postLoginRequest(loginBody: LoginBody): Flow<LoginResponse> = flow {
        emit(registerService.login(loginBody))
    }

    fun postRegisterRequest(registerBody: RegisterBody): Flow<RegisterResponse> = flow {
        emit(registerService.register(registerBody))

    }
}