package com.metafortech.calma.domain.mappers

import com.metafortech.calma.data.remote.login.LoginResponse
import com.metafortech.calma.domain.login.LoginResponseModel
import jakarta.inject.Inject

class LoginResponseMapperImpl @Inject constructor() : LoginResponseMapper{
    override fun invoke(loginResponse: LoginResponse): LoginResponseModel = LoginResponseModel(
        userToken = loginResponse.data.token,
        userName = loginResponse.data.user.name,
        userImageUrl = loginResponse.data.user.image,
        success = loginResponse.success,
        message = loginResponse.message
    )
}