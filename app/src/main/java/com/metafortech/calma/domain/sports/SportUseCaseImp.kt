package com.metafortech.calma.domain.sports

import com.metafortech.calma.data.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

class SportUseCaseImp @Inject constructor(
    private val authRepository: AuthRepository
) : SportUseCase {
    override fun invoke(): Flow<DomainSportState> = flow {
        authRepository.getSports().collect { sportResponse ->
            if (sportResponse.isSuccessful) {
                val body = sportResponse.body()
                if (body != null && body.success) {
                    emit(DomainSportState.OnSuccess(body))
                } else {
                    emit(DomainSportState.OnFailed(error = ("${body?.message} with code: ${body?.code}")))
                }
            } else {
                val errorMessage = sportResponse.errorBody()?.string()?.let { errorBodyString ->
                    try {
                        val errorJson = JSONObject(errorBodyString)
                        errorJson.optString(
                            "message",
                            "Unknown server error"
                        ) + " with code: " + errorJson.optString("code", "0")
                    } catch (_: Exception) {
                        "Unknown server error"
                    }
                }
                emit(DomainSportState.OnFailed(errorMessage))
            }
        }
    }.catch { throwable ->
        emit(DomainSportState.OnFailed("Network error: ${throwable.localizedMessage}"))

    }
}