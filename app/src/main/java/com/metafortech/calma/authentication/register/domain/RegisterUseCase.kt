package com.metafortech.calma.authentication.register.domain

import com.metafortech.calma.R
import com.metafortech.calma.authentication.data.Repository
import com.metafortech.calma.authentication.data.remote.register.RegisterBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(registerBody: RegisterBody) : Flow<DomainRegisterState> = flow {
        try {
            repository.postRegisterRequest(registerBody).catch {
                emit(DomainRegisterState.OnFailed(R.string.invalid_email_or_password))
            }.collect { registerResponse ->
                emit(DomainRegisterState.OnSuccess(registerResponse))
            }
        } catch (_: Exception) {
            emit(DomainRegisterState.OnFailed(R.string.something_went_wrong))

        }
    }
}
