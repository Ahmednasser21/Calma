package com.metafortech.calma.presentation.authentication.sport

data class SportSelectionUiState(
    val sports: List<Sport> = emptyList(),
    val selectedSportId: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDataLoaded: Boolean = false,
    val isSuccessfulRegister: Boolean = false
)
data class Sport(
    val id: Int,
    val nameAr: String,
    val nameEn: String,
    val nameFr: String,
    val imageUrl: String
)