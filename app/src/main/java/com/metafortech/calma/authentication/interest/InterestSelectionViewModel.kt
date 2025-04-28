package com.metafortech.calma.authentication.interest

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InterestSelectionViewModel @Inject constructor() : ViewModel() {
    private val _selectedInterest = mutableStateOf<String?>(null)
    val selectedInterest: State<String?> = _selectedInterest

    fun onInterestSelected(interest: String) {
        _selectedInterest.value = interest
    }
}