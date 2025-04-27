package com.metafortech.calma.authentication.register.domain

import com.metafortech.calma.authentication.data.Repository
import com.metafortech.calma.authentication.data.remote.register.RegisterBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(registerBody: RegisterBody): Flow<DomainRegisterState> = flow {
        try {
            repository.postRegisterRequest(registerBody)
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


