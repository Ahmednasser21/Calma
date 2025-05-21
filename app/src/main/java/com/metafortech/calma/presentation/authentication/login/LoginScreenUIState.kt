package com.metafortech.calma.presentation.authentication.login


data class LoginScreenUIState(
    val email: String = "",
    val password: String = "",
    val errorMessageResId: Int? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false,
    val loginError: String? = null
)
