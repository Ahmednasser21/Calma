package com.metafortech.calma.authentication.register.presentation

import androidx.annotation.StringRes

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
    val loginSuccess: Boolean = false,
    @StringRes val errorMessageResId: Int? = null,
)
