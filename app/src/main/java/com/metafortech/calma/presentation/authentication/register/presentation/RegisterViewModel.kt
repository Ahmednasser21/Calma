package com.metafortech.calma.presentation.authentication.register.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.R
import com.metafortech.calma.data.di.IODispatcher
import com.metafortech.calma.data.remote.presentation.register.RegisterBody
import com.metafortech.calma.presentation.authentication.GoogleSignInHandler
import com.metafortech.calma.presentation.authentication.register.domain.DomainRegisterState
import com.metafortech.calma.presentation.authentication.register.domain.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RegisterViewModule"

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val registerUseCase: RegisterUseCase,
    @IODispatcher val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameValueChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
        _uiState.value = _uiState.value.copy(errorMessageResId = null)

    }

    fun onEmailValueChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
        _uiState.value = _uiState.value.copy(errorMessageResId = null)
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phoneNumber)
        _uiState.value = _uiState.value.copy(errorMessageResId = null)
    }

    fun onCountryClick(country: Country) {
        _uiState.value = _uiState.value.copy(country = country)
        _uiState.value = _uiState.value.copy(isSheetOpen = false)
        _uiState.value = _uiState.value.copy(errorMessageResId = null)
    }

    fun onSheetOpenChange(isSheetOpen: Boolean) {
        _uiState.value = _uiState.value.copy(isSheetOpen = !isSheetOpen)
    }

    fun onSearchQueryChange(searchQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = searchQuery)
    }

    fun onPasswordValueChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
        _uiState.value = _uiState.value.copy(errorMessageResId = null)
    }

    fun onShowDatePickerChange(showDatePicker: Boolean) {
        _uiState.value = _uiState.value.copy(showDatePicker = !showDatePicker)

    }

    fun onBirthdayValueChange(birthday: String) {
        _uiState.value = _uiState.value.copy(birthday = birthday)
        _uiState.value = _uiState.value.copy(errorMessageResId = null)
    }

    fun onGenderClick(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
    }

    fun onRegisterClick() {
        _uiState.value = _uiState.value.copy(errorMessageResId = null, loginError = null)
        if (_uiState.value.name.isEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.name_required)
            }
        } else if (_uiState.value.email.isEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.email_is_required)
            }
        } else if (!Patterns.EMAIL_ADDRESS.matcher(_uiState.value.email)
                .matches() && !Patterns.PHONE.matcher(_uiState.value.email).matches()
        ) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.invalid_email_format)
            }
        } else if (_uiState.value.phoneNumber.isEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.phone_number_required)
            }

        } else if (_uiState.value.country.dialCode.isEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.country_required)
            }
        } else if (!Patterns.PHONE.matcher(_uiState.value.country.dialCode + _uiState.value.phoneNumber)
                .matches()
        ) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.phone_formate_error)
            }
        } else if (_uiState.value.password.isEmpty() && _uiState.value.email.isNotEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.password_required)
            }

        } else if (_uiState.value.birthday.isEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.birthday_required)
            }
        } else if (_uiState.value.gender.isEmpty()) {
            _uiState.update {
                it.copy(errorMessageResId = R.string.gender_required)
            }
        } else {
            _uiState.update {
                it.copy(isLoading = true)
            }
            _uiState.update {
                it.copy(errorMessageResId = null)
            }
            viewModelScope.launch(dispatcher) {
                registerUseCase.invoke(
                    RegisterBody(
                        _uiState.value.name,
                        _uiState.value.email,
                        (_uiState.value.country.code + _uiState.value.phoneNumber),
                        _uiState.value.password,
                        _uiState.value.gender,
                        _uiState.value.birthday
                    )
                ).collect { domainRegisterState ->
                    when (domainRegisterState) {
                        is DomainRegisterState.OnSuccess -> {
                            _uiState.update {
                                it.copy(isLoading = false, loginSuccess = true)
                            }

                        }

                        is DomainRegisterState.OnFailed -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessageResId = null,
                                    loginError = domainRegisterState.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun onLoginWithGoogleClick(context: Context) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        val activity = context as? Activity ?: return
        val googleSignInHandler by lazy { GoogleSignInHandler() }
        viewModelScope.launch(dispatcher) {
            try {
                val result = googleSignInHandler.signInWithGoogle(activity)
                val data = googleSignInHandler.handleSignIn(result).split("|")
                _uiState.update {
                    it.copy(isLoading = false,name = data[0], email = data[1],)
                }

            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error: ${e.message}", e)
            }
        }
    }


    fun onLoginWithFacebookClick() {


    }
}