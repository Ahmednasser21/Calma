package com.metafortech.calma.domain.register

import com.metafortech.calma.data.remote.register.RegisterBody
import kotlinx.coroutines.flow.Flow

interface RegisterUseCase {
    operator fun invoke(registerBody: RegisterBody): Flow<DomainRegisterState>

}