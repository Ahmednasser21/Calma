package com.metafortech.calma.data.remote.login

data class LoginResponse(
    val success: Boolean,
    val data: DataContent,
    val message: String,
    val code: Int
)

data class DataContent(
    val token: String,
    val user: User
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val dob: String,
    val gendar: String,
    val image: String
)

