package com.metafortech.calma.presentation.home.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.metafortech.calma.R
import com.metafortech.calma.data.local.AppPreferences
import com.metafortech.calma.utills.TimeFormater
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@UnstableApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    @ApplicationContext private val context: Context,
    private val timeFormater: TimeFormater
) : ViewModel() {
    private var audioPlayer: ExoPlayer? = null
    private var progressUpdateJob: Job? = null

    private val _homeState = MutableStateFlow<HomeScreenState>(HomeScreenState())
    val homeState = _homeState.asStateFlow().onStart {
        getStoredUserData()
        fetchPosts()
        initializeAudioPlayer()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = HomeScreenState()
    )

    private fun getStoredUserData() {
        _homeState.value = _homeState.value.copy(
            userImageUrl = appPreferences.getString(context.getString(R.string.user_image_url)),
            userName = appPreferences.getString(context.getString(R.string.name))
        )

    }
    fun fetchPosts() {
        val posts = SamplePosts.samplePosts
        _homeState.update { it.copy(posts = posts) }
    }

    @UnstableApi
    private fun initializeAudioPlayer() {
        audioPlayer = ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_READY -> {
                            _homeState.value = _homeState.value.copy(
                                audioPlayerState = _homeState.value.audioPlayerState.copy(
                                    isLoading = false,
                                    duration = duration
                                )
                            )
                            if (playWhenReady) {
                                startProgressUpdates()
                            } else {
                                stopProgressUpdates()
                            }
                        }
                        Player.STATE_BUFFERING -> {
                            _homeState.value = _homeState.value.copy(
                                audioPlayerState = _homeState.value.audioPlayerState.copy(
                                    isLoading = true
                                )
                            )
                        }
                        Player.STATE_ENDED -> {
                            _homeState.value = _homeState.value.copy(
                                audioPlayerState = _homeState.value.audioPlayerState.copy(
                                    isPlaying = false
                                )
                            )
                            stopProgressUpdates()
                        }

                        Player.STATE_IDLE -> {

                        }
                    }
                }

                override fun onPlayerError(error: PlaybackException) {
                    _homeState.value = _homeState.value.copy(
                        audioPlayerState = _homeState.value.audioPlayerState.copy(
                            isLoading = false,
                            error = error.message
                        )
                    )
                }
            })
        }
    }

    private fun startProgressUpdates() {
        stopProgressUpdates()
        progressUpdateJob = viewModelScope.launch {
            while (audioPlayer?.isPlaying == true) {
                audioPlayer?.let { player ->
                    updateAudioProgress(player.currentPosition, player.duration)
                }
                delay(1000)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }


    fun playAudio(audioUrl: String) {
        val currentState = _homeState.value.audioPlayerState

        if (currentState.currentAudioUrl != audioUrl) {
            stopAudio()

            val mediaItem = MediaItem.fromUri(audioUrl)
            audioPlayer?.apply {
                setMediaItem(mediaItem)
                prepare()
            }

            _homeState.value = _homeState.value.copy(
                audioPlayerState = AudioPlayerState(
                    isPlaying = true,
                    currentAudioUrl = audioUrl,
                    isLoading = true
                )
            )
        } else {

            _homeState.value = _homeState.value.copy(
                audioPlayerState = currentState.copy(isPlaying = true)
            )
        }

        audioPlayer?.play()
    }

    fun pauseAudio() {
        audioPlayer?.pause()
        _homeState.value = _homeState.value.copy(
            audioPlayerState = _homeState.value.audioPlayerState.copy(
                isPlaying = false
            )
        )
        stopProgressUpdates()
    }

    fun stopAudio() {
        audioPlayer?.stop()
        stopProgressUpdates()
        _homeState.value = _homeState.value.copy(
            audioPlayerState = AudioPlayerState()
        )
    }

    fun updateAudioProgress(position: Long, duration: Long) {
        val progress = if (duration > 0) position.toFloat() / duration.toFloat() else 0f
        _homeState.value = _homeState.value.copy(
            audioPlayerState = _homeState.value.audioPlayerState.copy(
                currentPosition = position,
                duration = duration,
                progress = progress,
                isLoading = false
            )
        )
    }

    fun seekAudio(position: Long) {
        audioPlayer?.seekTo(position)
        _homeState.value = _homeState.value.copy(
            audioPlayerState = _homeState.value.audioPlayerState.copy(
                currentPosition = position,
                progress = if (_homeState.value.audioPlayerState.duration > 0)
                    position.toFloat() / _homeState.value.audioPlayerState.duration.toFloat() else 0f
            )
        )
    }

    fun likePost(postId: String) {
        val currentPosts = _homeState.value.posts
        val updatedPosts = currentPosts.map { post ->
            if (post.id == postId) {
                post.copy(
                    isLiked = !post.isLiked,
                    likesCount = if (post.isLiked) post.likesCount - 1 else post.likesCount + 1
                )
            } else post
        }
        _homeState.value = _homeState.value.copy(posts = updatedPosts)
    }

    fun onScroll(visibleItems: List<Int>) {
        val currentPostIndex = _homeState.value.posts.indexOfFirst {
            it.uiMediaItems.any { media ->
                media.type == MediaType.AUDIO && media.url == _homeState.value.audioPlayerState.currentAudioUrl
            }
        }

        if (_homeState.value.audioPlayerState.isPlaying &&
            currentPostIndex != -1 && currentPostIndex !in visibleItems
        ) {
            pauseAudio()
        }
    }

    fun formatTime(timeInMillis: Long): String = timeFormater.formatTime(timeInMillis)

    override fun onCleared() {
        super.onCleared()
        stopProgressUpdates()
        audioPlayer?.release()
        audioPlayer = null
    }
}