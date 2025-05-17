package com.metafortech.calma.presentation.authentication.login

import androidx.annotation.StringRes

data class LoginScreenUIState(
    val email: String = "",
    val password: String = "",
    @StringRes val errorMessageResId: Int? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val loginError: String? = null
)
