package com.metafortech.calma.domain.mappers

import com.metafortech.calma.data.remote.login.LoginResponse
import com.metafortech.calma.domain.login.LoginResponseModel

interface LoginResponseMapper {
    operator fun invoke(loginResponse: LoginResponse): LoginResponseModel

}