package com.metafortech.calma.data.remote.register

data class RegisterResponse(
    val success: Boolean,
    val data: RegisterData,
    val message: String,
    val code: Int
)

data class RegisterData(
    val token: String,
    val user: RegisteredUser
)

data class RegisteredUser(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val dob: String,
    val code: Int,
    val gendar: String,
    val image: String
)


