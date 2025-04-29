package com.metafortech.calma.presentation.authentication.sport

import androidx.lifecycle.ViewModel
import com.metafortech.calma.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SportSelectionViewModel @Inject constructor() : ViewModel() {
    // UI State as StateFlow
    private val _uiState = MutableStateFlow(SportSelectionUiState())
    val uiState: StateFlow<SportSelectionUiState> = _uiState.asStateFlow()

    init {
        // Initialize with sports data
        loadSports()
    }

    private fun loadSports() {
        // Set loading state
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

            // Update UI state with loaded sports
            _uiState.update { it.copy(
                sports = sportsList,
                isLoading = false,
                error = null
            )}
        } catch (e: Exception) {
            // Handle any errors
            _uiState.update { it.copy(
                isLoading = false,
                error = e.message
            )}
        }
    }

    // Handle sport selection
    fun selectSport(sportId: String) {
        _uiState.update { it.copy(selectedSportId = sportId) }
    }

    // Handle next button click
    fun onNextClick(navigateToNext: () -> Unit) {
        // Validate selection
        if (_uiState.value.selectedSportId != null) {
            // Save selection if needed
            saveUserSportPreference(_uiState.value.selectedSportId!!)
            // Navigate to next screen
            navigateToNext()
        }
    }

    // Save user preference (would connect to repository in real app)
    private fun saveUserSportPreference(sportId: String) {
        // In a real app, this would save to DataStore or a backend API
        // Example implementation could be:
        // userPreferencesRepository.saveUserSportPreference(sportId)
    }
}