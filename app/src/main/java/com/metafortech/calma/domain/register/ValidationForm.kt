package com.metafortech.calma.domain.register

data class ValidationFormState(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val country: DomainCountry = DomainCountry("", ""),
    val password: String = "",
    val birthday: String = "",
    val gender: String = "",
)

data class DomainCountry(
    val name: String,
    val dialCode: String
)
