package com.metafortech.calma.data.remote.login

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val customer: Customer,
    val type: String,
    val income: Int,
    val token: String
)

data class Customer(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val birth_date: String,
    val gender: Int,
    val country_id: Int?,
    val code: String,
    val image: String?,
    val fcm_token: String?,
    val status: Int,
    val deleted_at: String?,
    val created_at: String,
    val updated_at: String
)
