package com.metafortech.calma.authentication.data.remote.login

data class LoginBody(
    val email_phone: String,
    val password: String,
    val is_social: String
)