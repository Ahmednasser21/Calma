package com.metafortech.calma.domain.register

import com.metafortech.calma.data.remote.register.RegisterResponse

sealed class DomainRegisterState {
    class OnSuccess(val registerResponse: RegisterResponse) : DomainRegisterState()
    class OnFailed( val error: String ) : DomainRegisterState()
    class OnLocalFailed( val errorMessageResId: Int ) : DomainRegisterState()

}