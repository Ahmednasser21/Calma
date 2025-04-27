package com.metafortech.calma.authentication.login.domain

import com.metafortech.calma.authentication.data.Repository
import com.metafortech.calma.authentication.data.remote.login.LoginBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginUseCase @Inject constructor(val repository: Repository) {
    operator fun invoke(loginBody: LoginBody): Flow<DomainLoginState> = flow {
        try {
            repository.postLoginRequest(loginBody).catch {throwable->
                emit(DomainLoginState.OnFailed(throwable.localizedMessage))
            }.collect { loginResponse ->
                if(loginResponse.isSuccessful){
                    val body = loginResponse.body()
                    if (body != null && body.status) {
                        emit(DomainLoginState.OnSuccess(body))
                    }else{
                        emit(DomainLoginState.OnFailed(error = body?.message ?: "Unknown error"))
                    }
                }else {
                    val errorMessage = loginResponse.errorBody()?.string()?.let { errorBodyString ->
                        try {
                            val errorJson = JSONObject(errorBodyString)
                            errorJson.optString("message", "Unknown server error")
                        } catch (e: Exception) {
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
