package com.metafortech.calma.presentation.authentication.register.domain

import com.metafortech.calma.data.remote.presentation.register.RegisterResponse

sealed class DomainRegisterState {
    class OnSuccess(val registerResponse: RegisterResponse) : DomainRegisterState()
    class OnFailed( val error: String ) : DomainRegisterState()

}