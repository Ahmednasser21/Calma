package com.metafortech.calma.authentication.login.domain

import androidx.annotation.StringRes
import com.metafortech.calma.authentication.data.remote.LoginResponse

sealed class DomainLoginState {
    data class OnSuccess(val loginResponse: LoginResponse) : DomainLoginState()
    data class OnFailed(@StringRes val error: Int? = null) : DomainLoginState()
}