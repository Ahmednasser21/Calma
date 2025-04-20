package com.metafortech.calma.login.data

import com.metafortech.calma.login.data.remote.LoginBody
import com.metafortech.calma.login.data.remote.LoginResponse
import com.metafortech.calma.login.data.remote.RegisterService
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
}