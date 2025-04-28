package com.metafortech.calma.authentication.verification

data class PhoneVerificationUiState(
    val codeValues: List<String> = listOf("", "", "", ""),
    val remainingTime: Int = 60,
    val isCodeComplete: Boolean = false,
    val errorMessage: String? = null
)
