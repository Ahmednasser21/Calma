package com.metafortech.calma.domain.register

sealed class ValidationState {
    data class Error(val errorMessageResId: Int) : ValidationState()
    object Success : ValidationState()
}