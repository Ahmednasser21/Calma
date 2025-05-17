package com.metafortech.calma.domain.interest

import com.metafortech.calma.data.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

class InterestUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : InterestUseCase {
    override fun invoke(): Flow<DomainInterestState> = flow {
        authRepository.getInterest().collect { interestResponse ->
            if (interestResponse.isSuccessful) {
                val body = interestResponse.body()
                if (body != null && body.success) {
                    emit(DomainInterestState.OnSuccess(body))
                } else {
                    emit(DomainInterestState.OnFailed(error = ("${body?.message} with code: ${body?.code}")))
                }

            } else {
                val errorMessage = interestResponse.errorBody()?.string()?.let { errorBodyString ->
                    try {
                        val errorJson = JSONObject(errorBodyString)
                        errorJson.optString("message", "Unknown server error") +
                                " with code: " +
                                errorJson.optString("code", "0")
                    } catch (_: Exception) {
                        "Unknown server error"

                    }
                }
                emit(DomainInterestState.OnFailed(errorMessage))
            }
        }
    }.catch { throwable ->
        emit(DomainInterestState.OnFailed("Network error: ${throwable.localizedMessage}"))
    }
}