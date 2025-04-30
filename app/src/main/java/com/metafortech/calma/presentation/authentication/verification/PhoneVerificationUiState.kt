package com.metafortech.calma.presentation.authentication.verification

data class PhoneVerificationUiState(
    val codeValues: List<String> = listOf("", "", "", ""),
    val remainingTime: Int = 60,
    val isCodeComplete: Boolean = false,
    val isLoading: Boolean = false,
    val isCodeVerified: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
