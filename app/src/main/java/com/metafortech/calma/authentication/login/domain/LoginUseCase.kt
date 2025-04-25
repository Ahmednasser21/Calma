package com.metafortech.calma.authentication.login.domain

import com.metafortech.calma.authentication.data.Repository
import com.metafortech.calma.authentication.data.remote.login.LoginBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import com.metafortech.calma.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(val repository: Repository) {
    operator fun invoke(loginBody: LoginBody): Flow<DomainLoginState> = flow {
        try {
            repository.postLoginRequest(loginBody).catch {
                emit(DomainLoginState.OnFailed(R.string.invalid_email_or_password))
            }.collect { loginResponse ->
                emit(DomainLoginState.OnSuccess(loginResponse))
            }
        } catch (_: Exception) {
            emit(DomainLoginState.OnFailed(R.string.something_went_wrong))

        }

    }
}
