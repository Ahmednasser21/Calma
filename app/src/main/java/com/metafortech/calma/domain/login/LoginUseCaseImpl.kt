package com.metafortech.calma.domain.login

import com.metafortech.calma.data.remote.login.LoginBody
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

        authRepository.postLoginRequest(loginBody).collect { loginResponse ->
            if (loginResponse.isSuccessful) {
                val body = loginResponse.body()
                if (body != null && body.success) {
                    emit(DomainLoginState.OnSuccess(body))
                } else {
                    emit(
                        DomainLoginState.OnFailed(
                            error = ("${body?.message} with code: ${body?.code}")
                        )
                    )
                }
            } else {
                val errorMessage = loginResponse.errorBody()?.string()?.let { errorBodyString ->
                    try {
                        val errorJson = JSONObject(errorBodyString)
                        errorJson.optString("message", "Unknown server error") +
                                " with code: " +
                                errorJson.optString("code", "0")
                    } catch (_: Exception) {
                        "Unknown server error"
                    }
                } ?: "Unknown server error"

                emit(DomainLoginState.OnFailed(errorMessage))
            }
        }
    }.catch { throwable ->
        emit(DomainLoginState.OnFailed("Network error: ${throwable.localizedMessage}"))
    }
}
