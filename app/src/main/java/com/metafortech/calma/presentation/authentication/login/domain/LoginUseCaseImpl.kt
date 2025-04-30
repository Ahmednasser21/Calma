package com.metafortech.calma.presentation.authentication.login.domain

import com.metafortech.calma.data.remote.presentation.login.LoginBody
import com.metafortech.calma.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCaseImpl @Inject constructor(private val authRepository: AuthRepository) :
    LoginUseCase {
    override operator fun invoke(loginBody: LoginBody): Flow<DomainLoginState> = flow {
        try {
            authRepository.postLoginRequest(loginBody).catch { throwable ->
                emit(DomainLoginState.OnFailed(throwable.localizedMessage))
            }.collect { loginResponse ->
                if (loginResponse.isSuccessful) {
                    val body = loginResponse.body()
                    if (body != null && body.status) {
                        emit(DomainLoginState.OnSuccess(body))
                    } else {
                        emit(DomainLoginState.OnFailed(error = body?.message ?: "Unknown error"))
                    }
                } else {
                    val errorMessage = loginResponse.errorBody()?.string()?.let { errorBodyString ->
                        try {
                            val errorJson = JSONObject(errorBodyString)
                            errorJson.optString("message", "Unknown server error")
                        } catch (_: Exception) {
                            "Unknown server error"
                        }
                    } ?: "Unknown server error"

                    emit(DomainLoginState.OnFailed(errorMessage))
                }
            }
        } catch (e: Exception) {
            emit(DomainLoginState.OnFailed(e.localizedMessage))

        }

    }
}
