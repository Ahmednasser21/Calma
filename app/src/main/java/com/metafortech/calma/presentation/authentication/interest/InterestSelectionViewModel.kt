package com.metafortech.calma.presentation.authentication.interest

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.R
import com.metafortech.calma.data.local.AppPreferences
import com.metafortech.calma.di.IODispatcher
import com.metafortech.calma.domain.interest.DomainInterestState
import com.metafortech.calma.domain.interest.InterestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InterestSelectionViewModel @Inject constructor(
    private val interestUseCase: InterestUseCase,
    private val appPreferences: AppPreferences,
    @ApplicationContext private val context: Context,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _interestUIState = MutableStateFlow(InterestScreenUiState())
    val interestUIState = _interestUIState.asStateFlow()
        .onStart {
            getInterest()
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            InterestScreenUiState().copy(isLoading = true)
        )

    fun getInterest() {
        viewModelScope.launch(dispatcher) {
            interestUseCase.invoke().collect { domainInterestState ->
                when (domainInterestState) {
                    is DomainInterestState.OnSuccess -> {
                        val interests = domainInterestState.interestResponse.data.map { interest ->
                            InterestUIItem(
                                id = interest.id,
                                interestNameAr = interest.name,
                                interestNameEn = interest.name_en,
                                interestNameFr = interest.name_fr
                            )
                        }
                        _interestUIState.update {
                            it.copy(
                                isLoading = false,
                                interests = interests.toMutableList()
                            )
                        }
                    }

                    is DomainInterestState.OnFailed -> {
                        _interestUIState.update {
                            it.copy(
                                isLoading = false,
                                error = domainInterestState.error
                            )
                        }
                    }
                }
            }
        }
    }

    fun onInterestSelected(interestItemId: Int) {
        _interestUIState.update { currentState ->
            currentState.copy(
                selectedInterestId = interestItemId
            )
        }
    }

    fun getSelectedLanguage(): String = appPreferences.getString(
        context.getString(R.string.selected_language),
        "en"
    )

}