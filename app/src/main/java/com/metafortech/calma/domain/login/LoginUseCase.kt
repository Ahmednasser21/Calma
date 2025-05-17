package com.metafortech.calma.domain.login

import com.metafortech.calma.data.remote.login.LoginBody
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
    operator fun invoke(loginBody: LoginBody): Flow<DomainLoginState>
}