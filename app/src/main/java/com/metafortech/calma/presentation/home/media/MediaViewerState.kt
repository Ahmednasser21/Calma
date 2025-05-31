package com.metafortech.calma.presentation.home.media

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.metafortech.calma.presentation.home.home.UIMediaItem

data class MediaViewerState(
    val uiMediaItems: List<UIMediaItem> = emptyList(),
    val currentMediaIndex: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isVideoPlaying: Boolean = false,
    val videoProgress: Float = 0f,
    val currentVideoPosition: Long = 0L,
    val videoDuration: Long = 0L,
    val isAudioPlaying: Boolean = false,
    val audioProgress: Float = 0f,
    val currentAudioPosition: Long = 0L,
    val audioDuration: Long = 0L,
    val savedVideoPositions: Map<String, Long> = emptyMap(),
    val savedAudioPositions: Map<String, Long> = emptyMap()
)
enum class MediaControlsSize(
    val playPauseSize: Dp,
    val backwardForwardSize: Dp,
    val playIconSize: Dp,
    val iconSize: Dp
) {
    MEDIUM(56.dp, 48.dp, 28.dp, 24.dp),
    LARGE(64.dp, 48.dp, 32.dp, 24.dp)
}