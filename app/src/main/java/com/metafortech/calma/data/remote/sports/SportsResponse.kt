package com.metafortech.calma.data.remote.sports

data class SportsResponse(
    val success: Boolean,
    val data: List<SportDto>,
    val message: String,
    val code: Int
)
data class SportDto(
    val id: Int,
    val name: String,
    val name_en: String,
    val name_fr: String,
    val image: String
)