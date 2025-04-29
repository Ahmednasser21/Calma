package com.metafortech.calma.data.remote.register

data class RegisterBody(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val gender: String,
    val birth_date: String,
)