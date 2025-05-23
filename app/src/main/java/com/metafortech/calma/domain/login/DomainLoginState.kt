package com.metafortech.calma.domain.login


sealed class DomainLoginState {
    data class OnSuccess(val loginResponseModel: LoginResponseModel) : DomainLoginState()
    data class OnFailed( val error: String? = null) : DomainLoginState()
}