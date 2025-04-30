package com.metafortech.calma.presentation.authentication.register.domain

import com.metafortech.calma.data.remote.presentation.register.RegisterBody
import kotlinx.coroutines.flow.Flow

interface RegisterUseCase {
    operator fun invoke(registerBody: RegisterBody): Flow<DomainRegisterState>

}