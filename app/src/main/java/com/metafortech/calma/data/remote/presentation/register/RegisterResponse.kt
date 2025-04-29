package com.metafortech.calma.data.remote.presentation.register

data class RegisterResponse(
    val status: Boolean,
    val message: String,
    val user: User,
    val token: String
)

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val gender: String,
    val birth_date: String,
    val status: String,
    val code: Int,
    val updated_at: String,
    val created_at: String,
    val id: Int
)


