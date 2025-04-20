package com.metafortech.calma.login.data.remote

data class LoginBody(
    val email_phone: String,
    val password: String,
    val is_social: String
)