package com.metafortech.calma.presentation.home.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.R
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                // Simulate API call
                delay(1000)
                val profileData = ProfileData(
                    name = "يوسف القاضي",
                    profileImageUrl = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face",
                    countryFlagUrl = "https://hatscripts.github.io/circle-flags/flags/jo.svg",
                    description = "عاشق لكرة القدم 🏆 | أطمح لتحسين اللياقة والمنافسة في البطولات 💪",
                    sportsType = "كرة قدم",
                    friendsCount = 22
                )
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    profileData = profileData
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun onEditProfileClick() {
        // Handle edit profile
    }

    fun onShareClick() {
        // Handle share
    }

    fun onBackClick() {
        // Handle back navigation
    }
    fun onTabSelected(index: Int){
        _uiState.value = _uiState.value.copy(selectedTap = index)
    }
}