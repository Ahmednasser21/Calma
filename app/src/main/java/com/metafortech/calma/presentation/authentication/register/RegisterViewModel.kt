package com.metafortech.calma.presentation.authentication.register

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.R
import com.metafortech.calma.data.local.AppPreferences
import com.metafortech.calma.di.IODispatcher
import com.metafortech.calma.domain.google.GoogleSignInUseCase
import com.metafortech.calma.domain.register.DomainCountry
import com.metafortech.calma.domain.register.DomainRegisterState
import com.metafortech.calma.domain.register.ValidationFormState
import com.metafortech.calma.domain.register.RegisterUseCase
import com.metafortech.calma.presentation.authentication.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val registerUseCase: RegisterUseCase,
    val googleSignInUseCase: GoogleSignInUseCase,
    val appPreferences: AppPreferences,
    @ApplicationContext val context: Context,
    @IODispatcher val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private var _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvents.asSharedFlow()

    fun onNameValueChange(name: String) {
        _uiState.update { it.copy(name = name, errorMessageResId = null) }

    }

    fun onEmailValueChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessageResId = null, registerError = null) }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.update {
            it.copy(
                phoneNumber = phoneNumber, errorMessageResId = null, registerError = null
            )
        }
    }

    fun onCountryClick(country: Country) {
        _uiState.update {
            it.copy(
                country = country,
                isSheetOpen = false,
                errorMessageResId = null,
                registerError = null
            )
        }
    }

    fun onSheetOpenChange(isSheetOpen: Boolean) {
        _uiState.update { it.copy(isSheetOpen = !isSheetOpen) }
    }

    fun onSearchQueryChange(searchQuery: String) {
        _uiState.update { it.copy(searchQuery = searchQuery) }
    }

    fun onPasswordValueChange(password: String) {
        _uiState.update {
            it.copy(
                password = password, errorMessageResId = null, registerError = null
            )
        }
    }

    fun onShowDatePickerChange(showDatePicker: Boolean) {
        _uiState.update { it.copy(showDatePicker = !showDatePicker) }

    }

    fun onBirthdayValueChange(birthday: String) {
        _uiState.update {
            it.copy(
                birthday = birthday, errorMessageResId = null, registerError = null
            )
        }
    }

    fun onGenderClick(gender: String) {
        _uiState.update { it.copy(gender = gender) }
    }

    fun onRegisterClick() {
        _uiState.update {
            it.copy(
                isLoading = true,
                errorMessageResId = null,
                registerError = null
            )
        }
        viewModelScope.launch(dispatcher) {
            register()

        }
    }

    suspend fun register() {
        registerUseCase.invoke(
            ValidationFormState(
                name = _uiState.value.name,
                email = _uiState.value.email,
                phoneNumber = _uiState.value.phoneNumber,
                birthday = _uiState.value.birthday,
                password = _uiState.value.password,
                gender = _uiState.value.gender,
                country = DomainCountry(
                    name = _uiState.value.country.name,
                    dialCode = _uiState.value.country.dialCode
                )
            ),
            lang = appPreferences.getString(context.getString(R.string.selected_language),"en")
        ).collect { result ->
            when (result) {
                is DomainRegisterState.OnSuccess -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registerError = null,
                            errorMessageResId = null
                        )
                    }
                    _navigationEvents.emit(
                        NavigationEvent.VerificationScreen(
                            _uiState.value.country.dialCode + _uiState.value.phoneNumber,
                            result.registerResponse.data.token
                        )
                    )
                    appPreferences.saveBoolean(context.getString(R.string.is_registered), true)
                }

                is DomainRegisterState.OnFailed -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registerError = result.error,
                            errorMessageResId = null
                        )
                    }
                }

                is DomainRegisterState.OnLocalFailed -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            registerError = null,
                            errorMessageResId = result.errorMessageResId
                        )
                    }
                }
            }
        }
    }

    fun onRegisterWithGoogleClick(context: Context) {
        val activity = context as? Activity ?: return

        _uiState.update {
            it.copy(isLoading = true, errorMessageResId = null, registerError = null)
        }

        viewModelScope.launch(dispatcher) {
            try {
                val (name, email) = googleSignInUseCase.signInWithGoogle(activity)
                _uiState.update {
                    it.copy(isLoading = false, name = name, email = email)
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessageResId = R.string.google_login_failed
                    )
                }
            }
        }

    }


    fun onLoginWithFacebookClick() {


    }
}