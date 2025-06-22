package com.metafortech.calma.presentation.home.profile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedTap: Int = 2,
    val profileData: ProfileData? = null
)

data class ProfileData(
    val name: String,
    val profileImageUrl: String,
    val countryFlagUrl: String,
    val description: String,
    val sportsType: String,
    val friendsCount: Int,
    val isOwnProfile: Boolean = true
)