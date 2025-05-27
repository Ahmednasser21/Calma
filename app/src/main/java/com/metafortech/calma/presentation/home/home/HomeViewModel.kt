package com.metafortech.calma.presentation.home.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.R
import com.metafortech.calma.data.local.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    @ApplicationContext private val context: Context
):ViewModel() {

    private val _homeState = MutableStateFlow<HomeScreenState>(HomeScreenState())
    val homeState = _homeState.asStateFlow().onStart {
        getStoredUserData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeScreenState(posts = SamplePosts.samplePosts)
    )

    private fun getStoredUserData(){
        _homeState.value = _homeState.value.copy(
            userImageUrl = appPreferences.getString(context.getString(R.string.user_image_url)),
            userName = appPreferences.getString(context.getString(R.string.name))
        )

    }
}