package com.metafortech.calma.presentation.authentication.sport

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.metafortech.calma.R
import com.metafortech.calma.data.di.IODispatcher
import com.metafortech.calma.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SportSelectionViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val authRepository: AuthRepository,
    @IODispatcher val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(SportSelectionUiState())
    val uiState: StateFlow<SportSelectionUiState> = _uiState.asStateFlow()

    init {
        loadSports()
    }

    private fun loadSports() {
        _uiState.update { it.copy(isLoading = true) }

        try {
            val sportsList = listOf(
                Sport(id = "football", imageResId = R.drawable.football, nameResId = R.string.football),
                Sport(id = "basketball", imageResId = R.drawable.basketball, nameResId = R.string.basketball),
                Sport(id = "volleyball", imageResId = R.drawable.volleyball, nameResId = R.string.volleyball),
                Sport(id = "ice_hockey", imageResId = R.drawable.ice_hockey, nameResId = R.string.ice_hockey),
                Sport(id = "baseball", imageResId = R.drawable.baseball, nameResId = R.string.baseball),
                Sport(id = "table_tennis", imageResId = R.drawable.table_tennis, nameResId = R.string.table_tennis)
            )

            _uiState.update { it.copy(
                sports = sportsList,
                isLoading = false,
                error = null
            )}
        } catch (e: Exception) {
            _uiState.update { it.copy(
                isLoading = false,
                error = e.message
            )}
        }
    }

    fun updateUserProfileWithInterestAndSport(navigate: () -> Unit){
        _uiState.update { it.copy(isSuccess = true) }
        if(_uiState.value.isSuccess){
            navigate()
        }
    }

    fun selectSport(sportId: String) {
        _uiState.update { it.copy(selectedSportId = sportId) }
    }

    fun onNextClick(navigate: () -> Unit) {
        _uiState.update { it.copy(isSuccess = false) }
        if (_uiState.value.selectedSportId != null) {
            updateUserProfileWithInterestAndSport(navigate)
        }
    }
}