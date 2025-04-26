package com.metafortech.calma.authentication.login.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.R
import com.metafortech.calma.authentication.GoogleSignInHandler
import com.metafortech.calma.authentication.data.di.IODispatcher
import com.metafortech.calma.authentication.data.remote.login.LoginBody
import com.metafortech.calma.authentication.login.domain.DomainLoginState
import com.metafortech.calma.authentication.login.domain.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModule"

@HiltViewModel
class LoginViewModule @Inject constructor(
    val loginUseCase: LoginUseCase,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
        _uiState.update {
            it.copy(errorMessageResId = null)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
        _uiState.update {
            it.copy(errorMessageResId = null)
        }
    }

    fun onLoginClick() {

        if (_uiState.value.email.isEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.email_required)
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email)
                .matches() && !Patterns.PHONE.matcher(_uiState.value.email).matches()
        ) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.invalid_email_format)
            }
        } else if (_uiState.value.password.isEmpty() && _uiState.value.email.isNotEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.password_required)
            }
        } else {
            _uiState.update {
                it.copy(isLoading = true)
            }
            _uiState.update {
                it.copy(errorMessageResId = null)
            }
            viewModelScope.launch(dispatcher) {
                login(LoginBody(uiState.value.email, uiState.value.password, "0"))
            }
        }
    }

    suspend fun login(loginBody: LoginBody) {
        loginUseCase.invoke(loginBody)
            .collect { domainLoginState ->
                when (domainLoginState) {
                    is DomainLoginState.OnSuccess -> {
                        _uiState.update {
                            it.copy(isLoading = false, loginSuccess = true)
                        }
                    }

                    is DomainLoginState.OnFailed -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessageResId = domainLoginState.error
                            )
                        }
                    }
                }
            }
    }

    fun onLoginWithGoogleClick(context: Context) {
        val activity = context as? Activity ?: return
        val googleSignInHandler by lazy { GoogleSignInHandler() }
        viewModelScope.launch(dispatcher) {
            try {
                val result = googleSignInHandler.signInWithGoogle(activity)
                val data = googleSignInHandler.handleSignIn(result).split("|")
                _uiState.update {
                    it.copy(email = data[1])
                }
                login(LoginBody(data[1], "", "1"))

            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error: ${e.message}", e)
            }
        }
    }

    fun onLoginWithFacebookClick() {


    }
}