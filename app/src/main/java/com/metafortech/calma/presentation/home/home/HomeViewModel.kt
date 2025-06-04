package com.metafortech.calma.presentation.home.home

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ConcurrentHashMap
import androidx.core.graphics.scale
import com.metafortech.calma.di.IODispatcher
import com.metafortech.calma.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher

@UnstableApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    @ApplicationContext private val context: Context,
    private val timeFormater: TimeFormater,
    @IODispatcher val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var audioPlayer: ExoPlayer? = null
    private var progressUpdateJob: Job? = null

    private val thumbnailCache = ConcurrentHashMap<String, String>()

    private val videoProcessingSemaphore = Semaphore(3)

    private val _homeState = MutableStateFlow<HomeScreenState>(HomeScreenState())
    val homeState = _homeState.asStateFlow().onStart {
        getStoredUserData()
        fetchPosts()
        initializeAudioPlayer()
        cleanupOldThumbnails()
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
        _homeState.value = _homeState.value.copy(posts = posts)

        viewModelScope.launch(mainDispatcher) {
            processMediaItemsOptimized(posts)
        }
    }

    private fun processMediaItemsOptimized(posts: List<PostModel>) {
        val audioJobs = mutableListOf<Job>()
        val videoJobs = mutableListOf<Job>()

        posts.forEach { post ->
            post.uiMediaItems.forEach { mediaItem ->
                when (mediaItem.type) {
                    MediaType.AUDIO -> {
                        audioJobs.add(viewModelScope.launch(ioDispatcher) {
                            val updatedItem = processMediaItemSafely(mediaItem)
                            updateMediaItemInPost(post.id, mediaItem.url, updatedItem)
                        })
                    }

                    MediaType.VIDEO -> {
                        videoJobs.add(viewModelScope.launch(ioDispatcher) {
                            videoProcessingSemaphore.withPermit {
                                val updatedItem = processVideoItemFast(mediaItem)
                                updateMediaItemInPost(post.id, mediaItem.url, updatedItem)
                            }
                        })
                    }

                    MediaType.IMAGE -> {

                    }
                }
            }
        }
    }

    private fun updateMediaItemInPost(
        postId: String,
        mediaUrl: String,
        updatedMediaItem: UIMediaItem,
    ) {
        _homeState.update { currentState ->
            val updatedPosts = currentState.posts.map { post ->
                if (post.id == postId) {
                    val updatedMediaItems = post.uiMediaItems.map { mediaItem ->
                        if (mediaItem.url == mediaUrl) updatedMediaItem else mediaItem
                    }
                    post.copy(uiMediaItems = updatedMediaItems)
                } else {
                    post
                }
            }
            currentState.copy(posts = updatedPosts)
        }
    }

    private suspend fun processVideoItemFast(mediaItem: UIMediaItem): UIMediaItem {
        return withContext(Dispatchers.IO) {
            val cachedThumbnail = thumbnailCache[mediaItem.url]

            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(mediaItem.url)

                val durationString =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val durationMs = durationString?.toLongOrNull() ?: 0L
                val formattedDuration =
                    if (durationMs > 0) timeFormater.formatTime(durationMs) else null

                val thumbnailUrl =
                    cachedThumbnail ?: generateOptimizedThumbnail(retriever, mediaItem)

                mediaItem.copy(
                    duration = formattedDuration,
                    thumbnailUrl = thumbnailUrl.toString()
                )
            } catch (_: Exception) {

                try {
                    val durationString =
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                    val durationMs = durationString?.toLongOrNull() ?: 0L
                    val formattedDuration =
                        if (durationMs > 0) timeFormater.formatTime(durationMs) else null
                    mediaItem.copy(duration = formattedDuration)
                } catch (_: Exception) {
                    mediaItem
                }
            } finally {
                try {
                    retriever.release()
                } catch (_: Exception) {

                }
            }
        }
    }

    private fun generateOptimizedThumbnail(
        retriever: MediaMetadataRetriever,
        mediaItem: UIMediaItem,
    ): String? {
        return try {

            val durationString =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val durationMs = durationString?.toLongOrNull() ?: 0L


            val targetTime = if (durationMs > 0) {
                minOf(durationMs * 0.1, 3000.0).toLong() * 1000
            } else {
                1000000L
            }

            val thumbnail = retriever.getFrameAtTime(
                targetTime,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC
            )

            if (thumbnail != null) {
                val thumbnailUrl = true.saveThumbnailToCache(thumbnail, mediaItem.id)
                if (thumbnailUrl != null) {
                    thumbnailCache[mediaItem.url] = thumbnailUrl
                }
                thumbnailUrl
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun processMediaItemSafely(mediaItem: UIMediaItem): UIMediaItem {
        return try {
            when (mediaItem.type) {
                MediaType.VIDEO -> {
                    processVideoItemFast(mediaItem)
                }

                MediaType.AUDIO -> {
                    processAudioItemFast(mediaItem)
                }

                MediaType.IMAGE -> mediaItem
            }
        } catch (_: Exception) {
            mediaItem
        }
    }

    private suspend fun processAudioItemFast(mediaItem: UIMediaItem): UIMediaItem {
        return withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(mediaItem.url)

                val durationString =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val durationMs = durationString?.toLongOrNull() ?: 0L
                val formattedDuration =
                    if (durationMs > 0) timeFormater.formatTime(durationMs) else null

                mediaItem.copy(duration = formattedDuration)
            } catch (_: Exception) {
                mediaItem
            } finally {
                try {
                    retriever.release()
                } catch (_: Exception) {

                }
            }
        }
    }

    private fun Boolean.saveThumbnailToCache(
        bitmap: Bitmap?,
        mediaId: String,
    ): String? {
        if (bitmap == null) return null

        return try {
            val cacheDir = File(this@HomeViewModel.context.cacheDir, "thumbnails")
            if (!cacheDir.exists()) {
                cacheDir.mkdirs()
            }

            val thumbnailFile = File(cacheDir, "thumb_$mediaId.jpg")

            if (thumbnailFile.exists() && thumbnailFile.length() > 0) {
                return thumbnailFile.absolutePath
            }

            val outputStream = FileOutputStream(thumbnailFile)

            val scaledBitmap = if (this && (bitmap.width > 300 || bitmap.height > 300)) {
                val scale = 300f / maxOf(bitmap.width, bitmap.height)
                val newWidth = (bitmap.width * scale).toInt()
                val newHeight = (bitmap.height * scale).toInt()
                bitmap.scale(newWidth, newHeight, false)
            } else {
                bitmap
            }

            val quality = if (this) 60 else 80
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.close()

            if (scaledBitmap != bitmap) {
                scaledBitmap.recycle()
            }

            thumbnailFile.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    fun processVisiblePostsFirst(visiblePostIds: List<String>) {
        viewModelScope.launch(mainDispatcher) {
            val currentPosts = _homeState.value.posts

            visiblePostIds.forEach { postId ->
                val post = currentPosts.find { it.id == postId }
                post?.uiMediaItems?.forEach { mediaItem ->
                    if (mediaItem.type == MediaType.VIDEO && mediaItem.thumbnailUrl.isNullOrEmpty()) {
                        launch(ioDispatcher) {
                            videoProcessingSemaphore.withPermit {
                                val updatedItem = processVideoItemFast(mediaItem)
                                updateMediaItemInPost(post.id, mediaItem.url, updatedItem)
                            }
                        }
                    }
                }
            }
        }
    }

    fun cleanupOldThumbnails() {
        viewModelScope.launch(ioDispatcher) {
            try {
                val cacheDir = File(context.cacheDir, "thumbnails")
                if (cacheDir.exists()) {
                    val files = cacheDir.listFiles() ?: return@launch
                    val currentTime = System.currentTimeMillis()
                    val maxAge = 2 * 24 * 60 * 60 * 1000L

                    files.forEach { file ->
                        if (currentTime - file.lastModified() > maxAge) {
                            file.delete()
                        }
                    }
                }
            } catch (_: Exception) {

            }
        }
    }


    fun onShowMoreClicked(postId: String) {
        _homeState.update { currentState ->
            val updatedPosts = currentState.posts.map { post ->
                if (post.id == postId) {
                    post.copy(isShowMoreClicked = !post.isShowMoreClicked)
                } else post
            }
            currentState.copy(posts = updatedPosts)
        }
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
        progressUpdateJob = viewModelScope.launch(mainDispatcher) {
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

        val visiblePostIds = visibleItems.mapNotNull { index ->
            _homeState.value.posts.getOrNull(index)?.id
        }
        processVisiblePostsFirst(visiblePostIds)
    }

    fun formatTime(timeInMillis: Long): String = timeFormater.formatTime(timeInMillis)

    fun onCommentClick(postId: String) {

        _homeState.update {
            it.copy(showComments = true, commentPostId = postId)
        }
    }
    fun onDismissComments(){
        _homeState.update {
            it.copy(showComments = false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopProgressUpdates()
        audioPlayer?.release()
        audioPlayer = null
        thumbnailCache.clear()
    }
}