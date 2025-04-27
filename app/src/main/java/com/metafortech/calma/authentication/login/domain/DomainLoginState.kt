package com.metafortech.calma.authentication.login.domain

import com.metafortech.calma.authentication.data.remote.login.LoginResponse

sealed class DomainLoginState {
    data class OnSuccess(val loginResponse: LoginResponse) : DomainLoginState()
    data class OnFailed( val error: String? = null) : DomainLoginState()
}