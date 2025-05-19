package com.metafortech.calma.presentation.authentication.register


data class RegisterUiState(
    val email: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val country: Country = CountryData.countries[0],
    val searchQuery: String = "",
    val isSheetOpen: Boolean = false,
    val showDatePicker: Boolean = false,
    val password: String = "",
    val birthday: String = "",
    val gender: String = "",
    val isLoading: Boolean = false,
    val registerError: String? = null,
    val errorMessageResId: Int? = null
)
