package com.metafortech.calma.data.remote.presentation.register

data class RegisterBody(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val gender: String,
    val birth_date: String,
)