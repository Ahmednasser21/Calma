package com.metafortech.calma.presentation.authentication.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.data.di.IODispatcher
import com.metafortech.calma.data.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneVerificationViewModel @Inject constructor(
    @MainDispatcher val dispatcher: CoroutineDispatcher,
    @IODispatcher val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PhoneVerificationUiState())
    val uiState: StateFlow<PhoneVerificationUiState> = _uiState.asStateFlow()

    private var countdownJob: Job? = null

    init {
        startCountdown()
    }

    fun onCodeValueChange(index: Int, value: String) {
        val newCodeValues = _uiState.value.codeValues.toMutableList()
        newCodeValues[index] = value

        val isComplete = newCodeValues.all { it.isNotEmpty() }

        _uiState.update { currentState ->
            currentState.copy(
                codeValues = newCodeValues,
                isCodeComplete = isComplete
            )
        }
    }

    fun onResendCode() {
        _uiState.update { currentState ->
            currentState.copy(
                codeValues = listOf("", "", "", ""),
                remainingTime = 60,
                isCodeComplete = false
            )
        }
        startCountdown()
        // Add API call to resend verification code
    }

    fun onNextClick() {
        if (_uiState.value.isCodeComplete) {
            val code = _uiState.value.codeValues.joinToString("")
            // Add API call to verify code
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch (dispatcher) {
            while (_uiState.value.remainingTime > 0) {
                delay(1000)
                _uiState.update { currentState ->
                    currentState.copy(remainingTime = currentState.remainingTime - 1)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}