package com.metafortech.calma.presentation.authentication.interest


data class InterestScreenUiState(
    val isLoading: Boolean = false,
    val interests: List<InterestUIItem> = emptyList(),
    val selectedInterestId: Int? = null,
    val error: String? = null,
)
data class InterestUIItem(
    val id: Int,
    val interestNameAr:String,
    val interestNameEn:String,
    val interestNameFr:String
)