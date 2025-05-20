package com.metafortech.calma.presentation.authentication.login

import android.app.Activity
import android.content.Context
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.R
import com.metafortech.calma.data.local.AppPreferences
import com.metafortech.calma.di.IODispatcher
import com.metafortech.calma.data.remote.login.LoginBody
import com.metafortech.calma.domain.google.GoogleSignInUseCase
import com.metafortech.calma.domain.login.DomainLoginState
import com.metafortech.calma.domain.login.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val loginUseCase: LoginUseCase,
    val googleSignInUseCase: GoogleSignInUseCase,
    val appPreferences: AppPreferences,
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
                it.copy(errorMessageResId = R.string.email_required, loginError = null)
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email)
                .matches() && !Patterns.PHONE.matcher(_uiState.value.email).matches()
        ) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.invalid_email_format, loginError = null)
            }
        } else if (_uiState.value.password.isEmpty() && _uiState.value.email.isNotEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.password_required, loginError = null)
            }
        } else {
            _uiState.update {
                it.copy(isLoading = true)
            }
            _uiState.update {
                it.copy(errorMessageResId = null)
            }
            viewModelScope.launch(dispatcher) {
                login(LoginBody(uiState.value.email, uiState.value.password))
            }
        }
    }

    suspend fun login(loginBody: LoginBody) {
        _uiState.update {
            it.copy(isLoading = true, errorMessageResId = null, loginError = null)
        }
        loginUseCase.invoke(loginBody)
            .collect { domainLoginState ->
                when (domainLoginState) {
                    is DomainLoginState.OnSuccess -> {
                        appPreferences.saveString("userToken",domainLoginState.loginResponse.data.token)
                        appPreferences.saveBoolean("isLoggedIn",true)
                        _uiState.update {
                            it.copy(isLoading = false, loginSuccess = true)
                        }
                    }

                    is DomainLoginState.OnFailed -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessageResId = null,
                                loginError = domainLoginState.error
                            )
                        }
                    }
                }
            }
    }

    fun onLoginWithGoogleClick(context: Context) {
        val activity = context as? Activity ?: return

        _uiState.update {
            it.copy(isLoading = true, errorMessageResId = null, loginError = null)
        }

        viewModelScope.launch(dispatcher) {
            try {
                val (_, email) = googleSignInUseCase.signInWithGoogle(activity)

                _uiState.update {
                    it.copy(email = email, isLoading = false)
                }

                login(LoginBody(email, ""))

            } catch (_: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessageResId = R.string.google_login_failed)
                }
            }
        }
    }

    fun onLoginWithFacebookClick() {


    }
}