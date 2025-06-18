package com.metafortech.calma.presentation.home.creat

import android.net.Uri
import com.metafortech.calma.presentation.home.media.MediaType

data class NewPostUiState(
    val postText: String = "",
    val userName: String = "",
    val userImageUrl: String = "",
    val mediaFiles: List<MediaFile> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPostSuccessful: Boolean = false
)
data class MediaFile(
    val uri: Uri,
    val type: MediaType,
    val thumbnailUri: Uri? = null,
    val duration: String? = null
)