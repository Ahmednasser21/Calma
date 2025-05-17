package com.metafortech.calma.data.remote.interest

data class InterestsResponse(
    val success: Boolean,
    val data: List<InterestDto>,
    val message: String,
    val code: Int
)
data class InterestDto(
    val id: Int,
    val name: String,
    val name_en: String,
    val name_fr: String
)