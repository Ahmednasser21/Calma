package com.metafortech.calma.data.remote.register

data class RegisterBody(
    val name: String,
    val email: String,
    val dob: String,
    val password: String,
    val phone: String,
    val gendar: String,
    val lang:String,
    val sports: List<Int>,
    val intersets: List<Int>
)