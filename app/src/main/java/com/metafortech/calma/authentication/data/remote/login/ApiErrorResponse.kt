package com.metafortech.calma.authentication.data.remote.login

data class ApiErrorResponse(
    val status: Boolean,
    val code: Int,
    val message: String,
    val data: Map<String, Any>?
)
