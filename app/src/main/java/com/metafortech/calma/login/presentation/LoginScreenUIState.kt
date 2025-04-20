package com.metafortech.calma.login.presentation

data class LoginScreenUIState(
    val email: String = "",
    val password: String = "",
    val errorMessages: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)
