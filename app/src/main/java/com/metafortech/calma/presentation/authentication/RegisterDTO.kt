package com.metafortech.calma.presentation.authentication

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDTO(
    val name: String,
    val email: String,
    val dop: String,
    val password: String,
    val phone: String,
    val gender: String,
    val lang: String? = null,
    val sports: List<Int>? = null,
    val interests: List<Int>? = null
)
