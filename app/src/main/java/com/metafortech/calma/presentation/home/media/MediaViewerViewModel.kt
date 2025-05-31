package com.metafortech.calma.presentation.home.media

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.metafortech.calma.presentation.home.home.MediaType
import com.metafortech.calma.presentation.home.home.UIMediaItem
import com.metafortech.calma.utills.TimeFormater
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MediaViewerViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val timeFormater: TimeFormater,
) : ViewModel() {
    private val _state = MutableStateFlow(MediaViewerState())
    val state: StateFlow<MediaViewerState> = _state.asStateFlow()
    private var videoPlayer: ExoPlayer? = null
    private var audioPlayer: ExoPlayer? = null
    private var videoProgressUpdateJob: Job? = null
    private var audioProgressUpdateJob: Job? = null

    init {
        initializeVideoPlayer()
        initializeAudioPlayer()
    }

    @OptIn(UnstableApi::class)
    private fun initializeVideoPlayer() {
        videoPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                videoDuration = duration
                            )
                            if (playWhenReady) {
                                startVideoProgressUpdates()
                            } else {
                                stopVideoProgressUpdates()
                            }
                        }

                        Player.STATE_BUFFERING -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }

                        Player.STATE_ENDED -> {
                            _state.value = _state.value.copy(isVideoPlaying = false)
                            stopVideoProgressUpdates()
                            // Clear saved position when video ends
                            getCurrentMedia()?.let { media ->
                                if (media.type == MediaType.VIDEO) {
                                    clearSavedVideoPosition(media.url)
                                }
                            }
                        }

                        Player.STATE_IDLE -> {

                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            })
        }
    }

    @OptIn(UnstableApi::class)
    private fun initializeAudioPlayer() {
        audioPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                audioDuration = duration
                            )
                            if (playWhenReady) {
                                startAudioProgressUpdates()
                            } else {
                                stopAudioProgressUpdates()
                            }
                        }

                        Player.STATE_BUFFERING -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }

                        Player.STATE_ENDED -> {
                            _state.value = _state.value.copy(isAudioPlaying = false)
                            stopAudioProgressUpdates()
                            // Clear saved position when audio ends
                            getCurrentMedia()?.let { media ->
                                if (media.type == MediaType.AUDIO) {
                                    clearSavedAudioPosition(media.url)
                                }
                            }
                        }

                        Player.STATE_IDLE -> {

                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            })
        }
    }

    private fun startVideoProgressUpdates() {
        stopVideoProgressUpdates()
        videoProgressUpdateJob = viewModelScope.launch {
            while (videoPlayer?.isPlaying == true) {
                videoPlayer?.let { player ->
                    updateVideoProgress(player.currentPosition, player.duration)
                    // Save current video position
                    saveCurrentVideoPosition()
                }
                delay(100)
            }
        }
    }

    private fun stopVideoProgressUpdates() {
        videoProgressUpdateJob?.cancel()
        videoProgressUpdateJob = null
        // Save position when stopping updates
        saveCurrentVideoPosition()
    }

    private fun stopProgressUpdates() {
        videoProgressUpdateJob?.cancel()
        videoProgressUpdateJob = null
        saveCurrentVideoPosition()
    }

    private fun startAudioProgressUpdates() {
        stopAudioProgressUpdates()
        audioProgressUpdateJob = viewModelScope.launch {
            while (audioPlayer?.isPlaying == true) {
                audioPlayer?.let { player ->
                    updateAudioProgress(player.currentPosition, player.duration)
                    // Save current audio position
                    saveCurrentAudioPosition()
                }
                delay(100)
            }
        }
    }

    private fun stopAudioProgressUpdates() {
        audioProgressUpdateJob?.cancel()
        audioProgressUpdateJob = null
        // Save position when stopping updates
        saveCurrentAudioPosition()
    }

    private fun saveCurrentVideoPosition() {
        videoPlayer?.let { player ->
            getCurrentMedia()?.let { media ->
                if (media.type == MediaType.VIDEO && player.currentPosition > 0) {
                    val currentVideoPositions = _state.value.savedVideoPositions.toMutableMap()
                    currentVideoPositions[media.url] = player.currentPosition
                    _state.value = _state.value.copy(savedVideoPositions = currentVideoPositions)
                }
            }
        }
    }

    private fun saveCurrentAudioPosition() {
        audioPlayer?.let { player ->
            getCurrentMedia()?.let { media ->
                if (media.type == MediaType.AUDIO && player.currentPosition > 0) {
                    val currentAudioPositions = _state.value.savedAudioPositions.toMutableMap()
                    currentAudioPositions[media.url] = player.currentPosition
                    _state.value = _state.value.copy(savedAudioPositions = currentAudioPositions)
                }
            }
        }
    }

    private fun getCurrentMedia(): UIMediaItem? {
        val currentState = _state.value
        return currentState.uiMediaItems.getOrNull(currentState.currentMediaIndex)
    }

    private fun stopAllMedia() {
        // Save positions before stopping
        saveCurrentVideoPosition()
        saveCurrentAudioPosition()

        videoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            }
            player.stop()
        }

        audioPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            }
            player.stop()
        }

        stopProgressUpdates()
        stopAudioProgressUpdates()
    }

    fun initializeMedia(uiMediaItems: List<UIMediaItem>, startIndex: Int = 0) {
        if (uiMediaItems.isEmpty()) return

        stopAllMedia()

        val safeStartIndex = startIndex.coerceIn(0, uiMediaItems.size - 1)

        _state.value = _state.value.copy(
            uiMediaItems = uiMediaItems,
            currentMediaIndex = safeStartIndex,
            isVideoPlaying = false,
            isAudioPlaying = false,
            videoProgress = 0f,
            audioProgress = 0f,
            currentVideoPosition = 0L,
            currentAudioPosition = 0L,
            error = null
        )

        val currentMedia = uiMediaItems.getOrNull(safeStartIndex)
        prepareMedia(currentMedia)
    }

    fun navigateToIndex(index: Int) {
        val currentState = _state.value
        val safeIndex = index.coerceIn(0, currentState.uiMediaItems.size - 1)

        if (safeIndex != currentState.currentMediaIndex) {
            stopAllMedia()

            _state.value = currentState.copy(
                currentMediaIndex = safeIndex,
                isVideoPlaying = false,
                isAudioPlaying = false,
                videoProgress = 0f,
                audioProgress = 0f,
                currentVideoPosition = 0L,
                currentAudioPosition = 0L,
                isLoading = false
            )

            val currentMedia = currentState.uiMediaItems.getOrNull(safeIndex)
            when (currentMedia?.type) {
                MediaType.VIDEO -> prepareVideo(currentMedia.url)
                MediaType.AUDIO -> prepareAudio(currentMedia.url)
                else -> {}
            }
        }
    }

    private fun prepareMedia(media: UIMediaItem?) {
        when (media?.type) {
            MediaType.VIDEO -> prepareVideo(media.url)
            MediaType.AUDIO -> prepareAudio(media.url)
            MediaType.IMAGE, null -> { /* No preparation needed */
            }
        }
    }

    private fun prepareAudio(audioUrl: String) {
        try {
            val mediaItem = MediaItem.fromUri(audioUrl)
            audioPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()

                // Restore saved audio position if available
                val savedPosition = _state.value.savedAudioPositions[audioUrl]
                if (savedPosition != null && savedPosition > 0) {
                    seekTo(savedPosition)
                    _state.value = _state.value.copy(
                        currentAudioPosition = savedPosition
                    )
                }
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = "Failed to load audio: ${e.message}"
            )
        }
    }

    private fun prepareVideo(url: String) {
        try {
            videoPlayer?.apply {
                setMediaItem(MediaItem.fromUri(url))
                prepare()

                // Restore saved video position if available
                val savedPosition = _state.value.savedVideoPositions[url]
                if (savedPosition != null && savedPosition > 0) {
                    seekTo(savedPosition)
                    _state.value = _state.value.copy(
                        currentVideoPosition = savedPosition
                    )
                }
            }
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = "Failed to load video: ${e.message}"
            )
        }
    }

    fun navigateToNextMedia() {
        val currentState = _state.value
        if (currentState.uiMediaItems.isEmpty()) return

        if (currentState.currentMediaIndex < currentState.uiMediaItems.size - 1) {
            navigateToIndex(currentState.currentMediaIndex + 1)
        }
    }

    fun navigateToPreviousMedia() {
        val currentState = _state.value
        if (currentState.uiMediaItems.isEmpty()) return

        if (currentState.currentMediaIndex > 0) {
            navigateToIndex(currentState.currentMediaIndex - 1)
        }
    }

    fun toggleVideoPlayback() {
        val currentState = _state.value
        val newPlayingState = !currentState.isVideoPlaying

        if (newPlayingState) {
            videoPlayer?.play()
        } else {
            videoPlayer?.pause()
        }

        _state.value = currentState.copy(isVideoPlaying = newPlayingState)
    }

    fun toggleAudioPlayback() {
        val currentState = _state.value
        val newPlayingState = !currentState.isAudioPlaying

        if (newPlayingState) {
            audioPlayer?.play()
        } else {
            audioPlayer?.pause()
        }

        _state.value = currentState.copy(isAudioPlaying = newPlayingState)
    }

    fun updateVideoProgress(position: Long, duration: Long) {
        val progress = if (duration > 0) position.toFloat() / duration.toFloat() else 0f
        _state.value = _state.value.copy(
            currentVideoPosition = position,
            videoDuration = duration,
            videoProgress = progress
        )
    }

    fun updateAudioProgress(position: Long, duration: Long) {
        val progress = if (duration > 0) position.toFloat() / duration.toFloat() else 0f
        _state.value = _state.value.copy(
            currentAudioPosition = position,
            audioDuration = duration,
            audioProgress = progress
        )
    }

    fun seekVideo(position: Long) {
        videoPlayer?.let { player ->
            val clampedPosition = position.coerceIn(0L, player.duration.coerceAtLeast(0L))
            player.seekTo(clampedPosition)
            _state.value = _state.value.copy(
                currentVideoPosition = clampedPosition,
                videoProgress = if (_state.value.videoDuration > 0)
                    clampedPosition.toFloat() / _state.value.videoDuration.toFloat() else 0f
            )
            // Save the new position
            saveCurrentVideoPosition()
        }
    }

    fun seekAudio(position: Long) {
        audioPlayer?.let { player ->
            val clampedPosition = position.coerceIn(0L, player.duration.coerceAtLeast(0L))
            player.seekTo(clampedPosition)
            _state.value = _state.value.copy(
                currentAudioPosition = clampedPosition,
                audioProgress = if (_state.value.audioDuration > 0)
                    clampedPosition.toFloat() / _state.value.audioDuration.toFloat() else 0f
            )
            // Save the new position
            saveCurrentAudioPosition()
        }
    }

    fun seekForward() {
        videoPlayer?.let { player ->
            val currentPosition = player.currentPosition
            val newPosition =
                (currentPosition + 10000).coerceAtMost(player.duration.coerceAtLeast(0L))
            seekVideo(newPosition)
        }
    }

    fun seekBackward() {
        videoPlayer?.let { player ->
            val currentPosition = player.currentPosition
            val newPosition = (currentPosition - 10000).coerceAtLeast(0L)
            seekVideo(newPosition)
        }
    }

    fun seekAudioForward() {
        audioPlayer?.let { player ->
            val currentPosition = player.currentPosition
            val newPosition =
                (currentPosition + 10000).coerceAtMost(player.duration.coerceAtLeast(0L))
            seekAudio(newPosition)
        }
    }

    fun seekAudioBackward() {
        audioPlayer?.let { player ->
            val currentPosition = player.currentPosition
            val newPosition = (currentPosition - 10000).coerceAtLeast(0L)
            seekAudio(newPosition)
        }
    }

    fun getVideoPlayer(): ExoPlayer? = videoPlayer

    fun formatTime(timeInMillis: Long): String = timeFormater.formatTime(timeInMillis)

    // Helper methods for position management
    fun clearSavedVideoPosition(videoUrl: String) {
        val currentVideoPositions = _state.value.savedVideoPositions.toMutableMap()
        currentVideoPositions.remove(videoUrl)
        _state.value = _state.value.copy(savedVideoPositions = currentVideoPositions)
    }

    fun clearSavedAudioPosition(audioUrl: String) {
        val currentAudioPositions = _state.value.savedAudioPositions.toMutableMap()
        currentAudioPositions.remove(audioUrl)
        _state.value = _state.value.copy(savedAudioPositions = currentAudioPositions)
    }

    fun clearAllSavedVideoPositions() {
        _state.value = _state.value.copy(savedVideoPositions = emptyMap())
    }

    fun clearAllSavedAudioPositions() {
        _state.value = _state.value.copy(savedAudioPositions = emptyMap())
    }

    fun clearAllSavedPositions() {
        _state.value = _state.value.copy(
            savedVideoPositions = emptyMap(),
            savedAudioPositions = emptyMap()
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopAllMedia()
        videoProgressUpdateJob?.cancel()
        audioProgressUpdateJob?.cancel()
        videoPlayer?.release()
        audioPlayer?.release()
        videoPlayer = null
        audioPlayer = null
    }
}