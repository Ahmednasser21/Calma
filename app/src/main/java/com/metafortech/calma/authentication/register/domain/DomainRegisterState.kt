package com.metafortech.calma.authentication.register.domain

import androidx.annotation.StringRes
import com.metafortech.calma.authentication.data.remote.register.RegisterResponse

sealed class DomainRegisterState {
    class OnSuccess(val registerResponse: RegisterResponse) : DomainRegisterState()
    class OnFailed(@StringRes val error: Int? = null) : DomainRegisterState()

}