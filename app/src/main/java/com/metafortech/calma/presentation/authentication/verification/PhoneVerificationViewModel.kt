package com.metafortech.calma.presentation.authentication.verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.data.di.IODispatcher
import com.metafortech.calma.data.di.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneVerificationViewModel @Inject constructor(
    @MainDispatcher val dispatcher: CoroutineDispatcher,
    @IODispatcher val ioDispatcher: CoroutineDispatcher,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var countdownJob: Job? = null

    private val _uiState = MutableStateFlow(PhoneVerificationUiState())
    val uiState: StateFlow<PhoneVerificationUiState> = _uiState.asStateFlow().onStart {
        startCountdown()
        sendVerificationCode(savedStateHandle.get<String>("phoneNumber") ?: "")
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        PhoneVerificationUiState()
    )

    fun sendVerificationCode(phoneNumber: String) {

    }

    fun verifyCode(code: String,navigate: () -> Unit) {
        _uiState.update { currentState ->
            currentState.copy(isSuccess = true)
        }
        if (_uiState.value.isSuccess) {
            countdownJob?.cancel()
            _uiState.update { currentState ->
                currentState.copy( remainingTime = 0)
            }
            navigate()
        }
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

        sendVerificationCode(savedStateHandle.get<String>("phoneNumber") ?: "")
    }

    fun onNextClick(navigate: () -> Unit) {
        if (_uiState.value.isCodeComplete) {
            _uiState.update { it.copy(isSuccess = false)}
            val code = _uiState.value.codeValues.joinToString("")
            verifyCode(code){navigate()}
        }
    }

    private fun startCountdown() {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch(dispatcher) {
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