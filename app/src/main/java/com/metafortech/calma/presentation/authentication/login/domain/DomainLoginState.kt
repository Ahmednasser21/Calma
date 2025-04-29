package com.metafortech.calma.presentation.authentication.login.domain

import com.metafortech.calma.data.remote.login.LoginResponse

sealed class DomainLoginState {
    data class OnSuccess(val loginResponse: LoginResponse) : DomainLoginState()
    data class OnFailed( val error: String? = null) : DomainLoginState()
}