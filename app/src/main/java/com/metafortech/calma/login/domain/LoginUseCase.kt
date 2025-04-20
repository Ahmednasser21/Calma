package com.metafortech.calma.login.domain

import com.metafortech.calma.login.data.Repository
import com.metafortech.calma.login.data.remote.LoginBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(val repository: Repository) {
    operator fun invoke(loginBody: LoginBody): Flow<DomainLoginState> = flow {
        try {
            repository.postLoginRequest(loginBody).catch {
                emit(DomainLoginState.OnFailed("Invalid email or password"))
            }.collect { loginResponse ->
                emit(DomainLoginState.OnSuccess(loginResponse))
            }
        } catch (e: Exception) {
            emit(DomainLoginState.OnFailed(e.message.toString()))

        }

    }
}
