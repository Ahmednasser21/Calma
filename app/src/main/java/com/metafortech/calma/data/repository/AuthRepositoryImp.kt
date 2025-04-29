package com.metafortech.calma.data.repository

import com.metafortech.calma.data.remote.APIService
import com.metafortech.calma.data.remote.presentation.login.LoginBody
import com.metafortech.calma.data.remote.presentation.login.LoginResponse
import com.metafortech.calma.data.remote.presentation.register.RegisterBody
import com.metafortech.calma.data.remote.presentation.register.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImp @Inject constructor(
    private val aPIService: APIService
) : AuthRepository {
    override fun postLoginRequest(loginBody: LoginBody): Flow<Response<LoginResponse>> =
        flow {
        emit(aPIService.login(loginBody))
    }

    override fun postRegisterRequest(registerBody: RegisterBody): Flow<Response<RegisterResponse>> =
        flow {
            emit(aPIService.register(registerBody))
        }
}