package com.metafortech.calma.login.domain

import com.metafortech.calma.login.data.remote.LoginResponse

sealed class DomainLoginState {
    data class OnSuccess(val loginResponse: LoginResponse) : DomainLoginState()
    data class OnFailed(val error: String) : DomainLoginState()
}