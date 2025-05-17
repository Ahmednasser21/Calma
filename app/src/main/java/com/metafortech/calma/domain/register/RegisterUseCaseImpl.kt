package com.metafortech.calma.domain.register

import com.metafortech.calma.data.remote.register.RegisterBody
import com.metafortech.calma.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : RegisterUseCase {
    override operator fun invoke(registerBody: RegisterBody): Flow<DomainRegisterState> = flow {

        authRepository.postRegisterRequest(registerBody).collect { response ->
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    emit(DomainRegisterState.OnSuccess(body))
                } else {
                    emit(
                        DomainRegisterState.OnFailed(
                            error = ("${body?.message} with code: ${body?.code}")
                        )
                    )
                }
            } else {
                val errorMessage = response.errorBody()?.string()?.let { errorBodyString ->
                    try {
                        val errorJson = JSONObject(errorBodyString)
                        errorJson.optString("message", "Unknown server error") +
                                " with code: " +
                                errorJson.optString("code", "0")
                    } catch (_: Exception) {
                        "Unknown server error"
                    }
                } ?: "Unknown server error"

                emit(DomainRegisterState.OnFailed(errorMessage))
            }
        }

    }.catch { throwable ->
        emit(
            DomainRegisterState.OnFailed(
                error = "Network error: ${throwable.localizedMessage}"
            )
        )
    }
}


