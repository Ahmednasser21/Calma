package com.metafortech.calma.presentation.authentication.sport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.data.remote.register.RegisterBody
import com.metafortech.calma.di.IODispatcher
import com.metafortech.calma.domain.register.DomainRegisterState
import com.metafortech.calma.domain.register.RegisterUseCase
import com.metafortech.calma.domain.sports.DomainSportState
import com.metafortech.calma.domain.sports.SportUseCase
import com.metafortech.calma.presentation.authentication.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SportSelectionViewModel @Inject constructor(
    private val sportUseCase: SportUseCase,
    private val registerUseCase: RegisterUseCase,
    @IODispatcher val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvents.asSharedFlow()

    private val _uiState = MutableStateFlow(SportSelectionUiState())
    val uiState: StateFlow<SportSelectionUiState> = _uiState.asStateFlow()
        .onStart {
            loadSports()
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            SportSelectionUiState().copy(isLoading = true, isSuccessfulRegister = false)
        )

    private suspend fun loadSports() {
        _uiState.update {
            it.copy(
                isLoading = true,
                isSuccessfulRegister = false,
                isDataLoaded = false,
                error = null
            )
        }

        sportUseCase.invoke().collect { domainSportState ->
            when (domainSportState) {
                is DomainSportState.OnSuccess -> {
                    _uiState.update {
                        it.copy(
                            sports = domainSportState.data.data
                                .map { sport ->
                                    Sport(
                                        id = sport.id,
                                        nameAr = sport.name,
                                        nameEn = sport.name_en,
                                        nameFr = sport.name_fr,
                                        imageUrl = sport.image
                                    )
                                },
                            isLoading = false
                        )
                    }

                }

                is DomainSportState.OnFailed -> {
                    _uiState.update {
                        it.copy(
                            error = domainSportState.error,
                            isLoading = false,
                            isDataLoaded = false
                        )
                    }
                }

            }
        }

    }

    fun register(registerBody: RegisterBody) {
        _uiState.update {
            it.copy(isLoading = true, error = null)
        }
        viewModelScope.launch(ioDispatcher) {
//            registerUseCase.invoke(registerBody).collect { domainRegisterState ->
//                when (domainRegisterState) {
//                    is DomainRegisterState.OnSuccess -> {
//                        _uiState.update {
//                            it.copy(isLoading = false, isSuccessfulRegister = true)
//                        }
//                        _navigationEvents.emit(NavigationEvent.VerificationScreen(registerBody.phone))
//                    }
//
//                    is DomainRegisterState.OnFailed -> {
//                        _uiState.update {
//                            it.copy(
//                                isLoading = false,
//                                error = domainRegisterState.error
//                            )
//                        }
//                    }
//                }
//            }
        }

    }

    fun selectSport(sportId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedSportId = sportId
            )
        }
    }
}