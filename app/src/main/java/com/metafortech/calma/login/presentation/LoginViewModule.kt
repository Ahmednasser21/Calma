package com.metafortech.calma.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.login.data.remote.LoginBody
import com.metafortech.calma.login.domain.DomainLoginState
import com.metafortech.calma.login.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModule @Inject constructor(val loginUseCase: LoginUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun onLoginClick() {
        _uiState.update {
            it.copy(isLoading = true)
        }
        _uiState.update {
            it.copy(errorMessages = null)
        }
        viewModelScope.launch {
            loginUseCase.invoke(LoginBody(uiState.value.email, uiState.value.password, "1"))
                .collect { domainLoginState ->
                    if (domainLoginState is DomainLoginState.OnSuccess) {
                        if (domainLoginState.loginResponse.message == "Success") {
                            _uiState.update {
                                it.copy(isLoading = false)
                            }
                            _uiState.update {
                                it.copy(loginSuccess = true)
                            }
                        }
                    } else if (domainLoginState is DomainLoginState.OnFailed) {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiState.update {
                            it.copy(errorMessages = domainLoginState.error)
                        }
                    }
                }
        }
    }
}