package com.metafortech.calma.data.repository

import com.metafortech.calma.data.remote.APIService
import com.metafortech.calma.data.remote.interest.InterestsResponse
import com.metafortech.calma.data.remote.interest.InterestsUpdateRequest
import com.metafortech.calma.data.remote.login.LoginBody
import com.metafortech.calma.data.remote.login.LoginResponse
import com.metafortech.calma.data.remote.register.RegisterBody
import com.metafortech.calma.data.remote.register.RegisterResponse
import com.metafortech.calma.data.remote.sports.SportsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImp @Inject constructor(
    private val aPIService: APIService
) : AuthRepository {
    override fun postLoginRequest(loginBody: LoginBody):
            Flow<Response<LoginResponse>> = flow {
        emit(aPIService.login(loginBody))
    }

    override fun getInterest():
            Flow<Response<InterestsResponse>> = flow {
        emit(aPIService.getInterest())
    }

    override fun getSports():
            Flow<Response<SportsResponse>> = flow {
        emit(aPIService.getSports())
    }

    override fun postUpdateInterests(
        request: InterestsUpdateRequest,
        token: String
    ): Flow<Response<SportsResponse>> = flow {
        emit(aPIService.updateInterestsAndSports(request, token))
    }


    override fun postRegisterRequest(registerBody: RegisterBody):
            Flow<Response<RegisterResponse>> = flow {
        emit(aPIService.register(registerBody))
    }
}