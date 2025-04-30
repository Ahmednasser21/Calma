package com.metafortech.calma.presentation.authentication.register.domain

import com.metafortech.calma.data.remote.presentation.register.RegisterBody
import com.metafortech.calma.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
): RegisterUseCase {
   override operator fun invoke(registerBody: RegisterBody): Flow<DomainRegisterState> = flow {
        try {
            authRepository.postRegisterRequest(registerBody)
                .catch { throwable ->
                    emit(
                        DomainRegisterState.OnFailed(
                            error = throwable.localizedMessage ?: "Unknown error"
                        )
                    )
                }
                .collect { response ->
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null && body.status) {
                            emit(DomainRegisterState.OnSuccess(body))
                        } else {
                            emit(
                                DomainRegisterState.OnFailed(
                                    error = body?.message ?: "Unknown error"
                                )
                            )
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string()?.let { errorBodyString ->
                            try {
                                val errorJson = JSONObject(errorBodyString)
                                errorJson.optString("message", "Unknown server error")
                            } catch (e: Exception) {
                                if (errorBodyString.startsWith("<!DOCTYPE", true)) {
                                    "This account is already registered"
                                } else {
                                    "Unknown server error"
                                }
                            }
                        } ?: "Unknown server error"

                        emit(DomainRegisterState.OnFailed(errorMessage))
                    }
                }
        } catch (e: Exception) {
            emit(DomainRegisterState.OnFailed(error = e.localizedMessage ?: "Unknown error"))
        }
    }
}


