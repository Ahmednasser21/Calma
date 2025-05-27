package com.metafortech.calma.data.remote


data class BaseResponse<T>(
    val success: Boolean,
    val data: T,
    val message: String,
    val code: Int
)
