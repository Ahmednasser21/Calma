package com.metafortech.calma.domain.sports

import com.metafortech.calma.data.remote.interest.InterestsUpdateRequest
import com.metafortech.calma.data.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject

class PostSportsAndInterestUpdateUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : PostSportsAndInterestUpdateUseCase {
    override suspend fun invoke(
        request: InterestsUpdateRequest,
        token: String
    ): Flow<DomainSportState> = flow {
        authRepository.postUpdateInterests(request, token).collect { response ->
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success) {
                    emit(DomainSportState.OnSuccess(data = body))
                } else {
                    emit(DomainSportState.OnFailed(error = ("${body?.message} with code: ${body?.code}")))
                }
            } else {
                val errorMessage = response.errorBody()?.string()?.let { errorBodyString ->
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