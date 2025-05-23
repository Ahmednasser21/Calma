package com.metafortech.calma.domain.login

data class LoginResponseModel(
    val userToken: String,
    val userName: String,
    val userImageUrl: String,
    val success: Boolean,
    val message: String
)
