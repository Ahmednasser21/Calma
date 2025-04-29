package com.metafortech.calma.data.remote.presentation.login

data class LoginBody(
    val email_phone: String,
    val password: String,
    val is_social: String
)