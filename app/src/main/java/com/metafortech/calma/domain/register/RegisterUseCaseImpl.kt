package com.metafortech.calma.domain.register

import com.metafortech.calma.data.repository.AuthRepository
import com.metafortech.calma.domain.mappers.FormStateToRegisterBodyMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val validationFormUseCase: ValidateFormUseCase,
    private val formStateToRegisterBodyMapper: FormStateToRegisterBodyMapper
) : RegisterUseCase {
    override operator fun invoke(validationFormState: ValidationFormState,lang:String): Flow<DomainRegisterState> = flow {

        val result = validationFormUseCase.invoke(validationFormState)
        when (result) {
            is ValidationState.Error -> {
                emit(DomainRegisterState.OnLocalFailed(result.errorMessageResId))
            }

            is ValidationState.Success -> {
                authRepository.postRegisterRequest(formStateToRegisterBodyMapper(validationFormState, lang))
                    .collect { response ->
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body != null && body.success) {
                                emit(DomainRegisterState.OnSuccess(body))
                            } else {
                                emit(
                                    DomainRegisterState.OnFailed(
                                        error = ("${body?.message} error code: ${body?.code}")
                                    )
                                )
                            }
                        } else {
                            val errorMessage =
                                response.errorBody()?.string()?.let { errorBodyString ->
                                    try {
                                        val errorJson = JSONObject(errorBodyString)
                                        errorJson.optString("message", "Unknown server error") +
                                                " error code: " +
                                                errorJson.optString("code", "0")
                                    } catch (_: Exception) {
                                        "Unknown server error"
                                    }
                                } ?: "Unknown server error"

                            emit(DomainRegisterState.OnFailed(errorMessage))
                        }
                    }
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


