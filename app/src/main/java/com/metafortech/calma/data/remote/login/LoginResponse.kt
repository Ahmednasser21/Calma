package com.metafortech.calma.data.remote.login

data class LoginResponse(
    val success: Boolean,
    val data: Data,
    val message: String,
    val code: Int
)

data class Data(
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
    val image: String,
    val cover_image: String,
    val intersets: List<Interest>,
    val sports: List<Sport>,
    val stated: List<Any>,
    val count_friends: Int
)

data class Interest(
    val id: Int,
    val name_ar: String,
    val name_en: String,
    val name_fr: String,
    val created_at: String,
    val updated_at: String,
    val pivot: InterestPivot
)

data class InterestPivot(
    val user_id: Int,
    val interest_id: Int
)

data class Sport(
    val id: Int,
    val name_ar: String,
    val name_en: String,
    val name_fr: String,
    val image: String,
    val created_at: String,
    val updated_at: String,
    val pivot: SportPivot
)

data class SportPivot(
    val user_id: Int,
    val sport_id: Int
)