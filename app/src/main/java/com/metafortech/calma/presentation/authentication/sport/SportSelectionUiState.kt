package com.metafortech.calma.presentation.authentication.sport

data class SportSelectionUiState(
    val sports: List<Sport> = emptyList(),
    val selectedSportId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)