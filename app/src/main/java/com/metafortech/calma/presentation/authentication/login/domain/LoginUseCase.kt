package com.metafortech.calma.presentation.authentication.login.domain

import com.metafortech.calma.data.remote.presentation.login.LoginBody
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
    operator fun invoke(loginBody: LoginBody): Flow<DomainLoginState>
}