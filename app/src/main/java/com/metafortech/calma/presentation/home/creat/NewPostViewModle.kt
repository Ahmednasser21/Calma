package com.metafortech.calma.presentation.home.creat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.metafortech.calma.R
import com.metafortech.calma.data.local.AppPreferences
import com.metafortech.calma.presentation.home.media.MediaType
import com.metafortech.calma.utills.MediaMetadataExtractor
import com.metafortech.calma.utills.TimeFormater
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val mediaMetadataExtractor: MediaMetadataExtractor,
    private val timeFormater: TimeFormater,
    private val appPreferences: AppPreferences,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewPostUiState())
    val uiState = _uiState.asStateFlow().onStart {
        getStoredUserData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = NewPostUiState()
    )

    private fun getStoredUserData() {
        _uiState.value = _uiState.value.copy(
            userImageUrl = appPreferences.getString(context.getString(R.string.user_image_url)),
            userName = appPreferences.getString(context.getString(R.string.name))
        )
    }

    fun updatePostText(text: String) {
        _uiState.value = _uiState.value.copy(
            postText = text,
            error = null
        )
    }

    fun addMediaFile(mediaFile: MediaFile) {
        val currentFiles = _uiState.value.mediaFiles.toMutableList()

        if (currentFiles.size >= 10) {
            _uiState.value = _uiState.value.copy(
                error = context.getString(R.string.you_cannot_add_more_than_10_files)
            )
            return
        }

        when (mediaFile.type) {
            MediaType.VIDEO -> addVideoWithMetadata(mediaFile.uri)
            MediaType.AUDIO -> addAudioWithDuration(mediaFile.uri)
            MediaType.IMAGE -> {
                currentFiles.add(mediaFile)
                _uiState.value = _uiState.value.copy(
                    mediaFiles = currentFiles,
                    error = null
                )
            }
        }
    }

    private fun addVideoWithMetadata(videoUri: Uri) {
        val videoFile = MediaFile(
            uri = videoUri,
            type = MediaType.VIDEO,
            thumbnailUri = null,
            duration = null
        )

        val currentFiles = _uiState.value.mediaFiles.toMutableList()
        currentFiles.add(videoFile)
        _uiState.value = _uiState.value.copy(
            mediaFiles = currentFiles,
            error = null
        )

        viewModelScope.launch {
            val metadata = mediaMetadataExtractor.extractVideoMetadata(videoUri)

            val updatedFiles = _uiState.value.mediaFiles.toMutableList()
            val videoIndex = updatedFiles.indexOfFirst {
                it.uri == videoUri && it.type == MediaType.VIDEO
            }

            if (videoIndex != -1) {
                metadata.duration?.let {
                    updatedFiles[videoIndex] = updatedFiles[videoIndex].copy(
                        thumbnailUri = metadata.thumbnailUri,
                        duration = timeFormater.formatTime(it)
                    )
                }

                _uiState.value = _uiState.value.copy(
                    mediaFiles = updatedFiles
                )
            }
        }
    }

    private fun addAudioWithDuration(audioUri: Uri) {
        val audioFile = MediaFile(
            uri = audioUri,
            type = MediaType.AUDIO,
            duration = null
        )

        val currentFiles = _uiState.value.mediaFiles.toMutableList()
        currentFiles.add(audioFile)
        _uiState.value = _uiState.value.copy(
            mediaFiles = currentFiles,
            error = null
        )

        viewModelScope.launch {
            val duration = mediaMetadataExtractor.extractAudioDuration(audioUri)

            val updatedFiles = _uiState.value.mediaFiles.toMutableList()
            val audioIndex = updatedFiles.indexOfFirst {
                it.uri == audioUri && it.type == MediaType.AUDIO
            }

            if (audioIndex != -1) {
                duration?.let {
                    updatedFiles[audioIndex] = updatedFiles[audioIndex].copy(
                        duration = timeFormater.formatTime(it)
                    )
                }

                _uiState.value = _uiState.value.copy(
                    mediaFiles = updatedFiles
                )
            }
        }
    }

    fun addVideoWithThumbnail(videoUri: Uri) {
        addVideoWithMetadata(videoUri)
    }

    fun createPost() {
        val currentState = _uiState.value

        if (currentState.postText.isBlank() && currentState.mediaFiles.isEmpty()) {
            _uiState.value = currentState.copy(
                error = context.getString(R.string.at_least_one_text_or_file_must_be_added)
            )
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = currentState.copy(
                    isLoading = true,
                    error = null
                )

                _uiState.value = currentState.copy(
                    isLoading = false,
                    isPostSuccessful = true,
                    error = null
                )

            } catch (_: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = "UnKnow Error"
                )
            }
        }
    }

    fun removeMediaFile(mediaFile: MediaFile) {
        val currentFiles = _uiState.value.mediaFiles.toMutableList()
        currentFiles.remove(mediaFile)

        if (mediaFile.type == MediaType.VIDEO) {
            mediaMetadataExtractor.cleanupThumbnail(mediaFile.thumbnailUri)
        }

        _uiState.value = _uiState.value.copy(
            mediaFiles = currentFiles,
            error = null
        )
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.value.mediaFiles.forEach { mediaFile ->
            if (mediaFile.type == MediaType.VIDEO) {
                mediaMetadataExtractor.cleanupThumbnail(mediaFile.thumbnailUri)
            }
        }
    }
}