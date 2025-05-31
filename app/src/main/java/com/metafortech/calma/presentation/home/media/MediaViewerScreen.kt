package com.metafortech.calma.presentation.home.media

import androidx.annotation.OptIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil3.compose.AsyncImage
import com.metafortech.calma.R
import com.metafortech.calma.presentation.AnimatedVisibility
import com.metafortech.calma.presentation.AudioFileIcon
import com.metafortech.calma.presentation.ClickableArea
import com.metafortech.calma.presentation.GradientBackground
import com.metafortech.calma.presentation.LoadingIndicator
import com.metafortech.calma.presentation.MediaBackButton
import com.metafortech.calma.presentation.MediaControls
import com.metafortech.calma.presentation.MediaCounter
import com.metafortech.calma.presentation.MediaProgressBar
import com.metafortech.calma.presentation.SwipeDetector
import com.metafortech.calma.presentation.home.home.MediaType
import com.metafortech.calma.presentation.home.home.UIMediaItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun MediaViewerScreen(
    modifier: Modifier = Modifier,
    state: MediaViewerState,
    onBackClick: () -> Unit,
    onNavigateToNext: () -> Unit,
    onNavigateToPrevious: () -> Unit,
    onNavigateToIndex: (Int) -> Unit,
    onToggleVideoPlayback: () -> Unit,
    onSeekVideo: (Long) -> Unit,
    onSeekVideoForward: () -> Unit,
    onSeekVideoBackward: () -> Unit,
    onToggleAudioPlayback: () -> Unit,
    onSeekAudio: (Long) -> Unit,
    onSeekAudioForward: () -> Unit,
    onSeekAudioBackward: () -> Unit,
    getVideoPlayer: () -> ExoPlayer?,
    formatTime: (Long) -> String,
) {
    var showNavigation by remember { mutableStateOf(true) }
    val hideNavigationJob = remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val currentMedia = if (state.uiMediaItems.isNotEmpty()) {
        state.uiMediaItems[state.currentMediaIndex]
    } else null

    val isCurrentMediaVideo = currentMedia?.type == MediaType.VIDEO

    LaunchedEffect(showNavigation, isCurrentMediaVideo) {
        if (showNavigation && isCurrentMediaVideo) {
            hideNavigationJob.value?.cancel()
            hideNavigationJob.value = coroutineScope.launch {
                delay(3000)
                showNavigation = false
            }
        }
    }

    LaunchedEffect(state.currentMediaIndex, isCurrentMediaVideo) {
        if (!isCurrentMediaVideo) {
            showNavigation = true
            hideNavigationJob.value?.cancel()
        } else {
            showNavigation = true
        }
    }

    SwipeDetector(
        onSwipeLeft = {
            if (state.currentMediaIndex > 0) {
                onNavigateToPrevious()
                if (isCurrentMediaVideo) {
                    showNavigation = true
                }
            }
        },
        onSwipeRight = {
            if (state.currentMediaIndex < state.uiMediaItems.size - 1) {
                onNavigateToNext()
                if (isCurrentMediaVideo) {
                    showNavigation = true
                }
            }
        },
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        ClickableArea(
            onClick = {
                if (isCurrentMediaVideo) {
                    showNavigation = !showNavigation
                }
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.uiMediaItems.isNotEmpty()) {
                    when (currentMedia?.type) {
                        MediaType.IMAGE -> {
                            ZoomableImageWithSwipe(
                                imageUrl = currentMedia.url,
                                onSwipeLeft = {
                                    onNavigateToNext()

                                },
                                onSwipeRight = {
                                    onNavigateToPrevious()
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        MediaType.VIDEO -> {
                            UpdatedVideoPlayerWithSwipe(
                                modifier = Modifier.fillMaxSize(),
                                isPlaying = state.isVideoPlaying,
                                progress = state.videoProgress,
                                currentPosition = state.currentVideoPosition,
                                duration = state.videoDuration,
                                onPlayPause = onToggleVideoPlayback,
                                onSeek = onSeekVideo,
                                onSeekForward = onSeekVideoForward,
                                onSeekBackward = onSeekVideoBackward,
                                exoPlayer = getVideoPlayer(),
                                hasMultipleMedia = state.uiMediaItems.size > 1,
                                showControls = showNavigation,
                                onControlsVisibilityChange = { visible ->
                                    showNavigation = visible
                                },
                                onSwipeLeft = {
                                    if (state.currentMediaIndex < state.uiMediaItems.size - 1) {
                                        onNavigateToNext()
                                        showNavigation = true
                                    }
                                },
                                onSwipeRight = {
                                    if (state.currentMediaIndex > 0) {
                                        onNavigateToPrevious()
                                        showNavigation = true
                                    }
                                },
                                formatTime = formatTime
                            )
                        }

                        MediaType.AUDIO -> {
                            AudioVisualizationWithSwipe(
                                modifier = Modifier.fillMaxSize(),
                                isPlaying = state.isAudioPlaying,
                                progress = state.audioProgress,
                                currentPosition = state.currentAudioPosition,
                                duration = state.audioDuration,
                                onPlayPause = onToggleAudioPlayback,
                                onSeek = onSeekAudio,
                                onSeekForward = onSeekAudioForward,
                                onSeekBackward = onSeekAudioBackward,
                                onSwipeLeft = {
                                    if (state.currentMediaIndex < state.uiMediaItems.size - 1) {
                                        onNavigateToNext()
                                    }
                                },
                                onSwipeRight = {
                                    if (state.currentMediaIndex > 0) {
                                        onNavigateToPrevious()
                                    }
                                },
                                formatTime = formatTime

                            )
                        }

                        null -> {}
                    }
                }

                if (state.isLoading) {
                    LoadingIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                val shouldShowTopBar = !isCurrentMediaVideo || showNavigation

                AnimatedVisibility(visible = shouldShowTopBar) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .statusBarsPadding(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MediaBackButton(onBackClick = onBackClick)

                        if (state.uiMediaItems.size > 1) {
                            MediaCounter(
                                currentIndex = state.currentMediaIndex,
                                totalCount = state.uiMediaItems.size
                            )
                        }
                    }
                }

                if (state.uiMediaItems.size > 1) {
                    val shouldShowThumbnails = !isCurrentMediaVideo || showNavigation

                    AnimatedVisibility(
                        visible = shouldShowThumbnails,
                        enter = slideInVertically { it } + fadeIn(),
                        exit = slideOutVertically { it } + fadeOut(),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        LazyRow(
                            modifier = Modifier
                                .padding(16.dp)
                                .background(
                                    Color.Black.copy(alpha = 0.7f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(state.uiMediaItems) { index, mediaItem ->
                                MediaThumbnail(
                                    uiMediaItem = mediaItem,
                                    isSelected = index == state.currentMediaIndex,
                                    onClick = {
                                        onNavigateToIndex(index)
                                        if (isCurrentMediaVideo) {
                                            showNavigation = true
                                        }
                                    },
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ZoomableImageWithSwipe(
    imageUrl: String,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isZoomed by remember { mutableStateOf(false) }

    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        val newScale = (scale * zoomChange).coerceIn(0.5f, 5f)
        scale = newScale
        isZoomed = newScale > 1.1f

        offsetX = (offsetX + offsetChange.x).coerceIn(-300f, 300f)
        offsetY = (offsetY + offsetChange.y).coerceIn(-300f, 300f)
    }

    val doubleTapGesture = remember {
        {
            if (scale > 1.1f) {
                scale = 1f
                offsetX = 0f
                offsetY = 0f
                isZoomed = false
            } else {
                scale = 2.5f
                isZoomed = true
            }
        }
    }

    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.zoomable_image),
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offsetX,
                translationY = offsetY
            )
            .transformable(state = transformableState)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { doubleTapGesture() }
                )
            }
            .pointerInput(isZoomed) {

                if (!isZoomed) {
                    var totalDragX = 0f
                    var hasNavigated = false

                    detectDragGestures(
                        onDragStart = {
                            totalDragX = 0f
                            hasNavigated = false
                        },
                        onDragEnd = {
                            totalDragX = 0f
                            hasNavigated = false
                        }
                    ) { _, dragAmount ->
                        totalDragX += dragAmount.x
                        val swipeThreshold = 50f

                        if (!hasNavigated && abs(totalDragX) > abs(dragAmount.y * 1.5f)) {
                            if (totalDragX > swipeThreshold) {
                                onSwipeLeft()
                                hasNavigated = true
                            } else if (totalDragX < -swipeThreshold) {
                                onSwipeRight()
                                hasNavigated = true
                            }
                        }
                    }
                }
            }
            .fillMaxSize(),
        contentScale = ContentScale.Fit
    )
}

@OptIn(UnstableApi::class)
@Composable
fun UpdatedVideoPlayerWithSwipe(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    progress: Float,
    currentPosition: Long,
    duration: Long,
    onPlayPause: () -> Unit,
    onSeek: (Long) -> Unit,
    onSeekForward: () -> Unit,
    onSeekBackward: () -> Unit,
    exoPlayer: ExoPlayer?,
    hasMultipleMedia: Boolean = false,
    showControls: Boolean = true,
    onControlsVisibilityChange: (Boolean) -> Unit = {},
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    formatTime: (Long) -> String
) {
    var internalShowControls by remember { mutableStateOf(true) }
    val hideControlsJob = remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(showControls) {
        internalShowControls = showControls
    }

    LaunchedEffect(internalShowControls) {
        if (internalShowControls) {
            hideControlsJob.value?.cancel()
            hideControlsJob.value = coroutineScope.launch {
                delay(3000)
                internalShowControls = false
                onControlsVisibilityChange(false)
            }
        }
    }

    SwipeDetector(
        onSwipeLeft = onSwipeLeft,
        onSwipeRight = onSwipeRight,
        modifier = modifier
    ) {
        ClickableArea(
            onClick = {
                internalShowControls = !internalShowControls
                onControlsVisibilityChange(internalShowControls)
            },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            player = exoPlayer
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                AnimatedVisibility(
                    visible = internalShowControls,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = if (hasMultipleMedia && showControls) 84.dp else 0.dp)
                ) {
                    GradientBackground(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        MediaProgressBar(
                            modifier = Modifier.fillMaxWidth(),
                            progress = progress,
                            currentPosition = currentPosition,
                            duration = duration,
                            onSeek = onSeek,
                            formatTime = formatTime
                        )
                    }
                }

                AnimatedVisibility(
                    visible = internalShowControls,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    MediaControls(
                        isPlaying = isPlaying,
                        onPlayPause = onPlayPause,
                        onSeekBackward = onSeekBackward,
                        onSeekForward = onSeekForward,
                        controlsSize = MediaControlsSize.LARGE
                    )
                }
            }
        }
    }
}

@Composable
fun AudioVisualizationWithSwipe(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    progress: Float = 0f,
    currentPosition: Long = 0L,
    duration: Long = 0L,
    onPlayPause: () -> Unit = {},
    onSeek: (Long) -> Unit = {},
    onSeekForward: () -> Unit = {},
    onSeekBackward: () -> Unit = {},
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    formatTime: (Long) -> String
) {
    var showControls by remember { mutableStateOf(true) }
    val hideControlsJob = remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(showControls) {
        if (showControls) {
            hideControlsJob.value?.cancel()
            hideControlsJob.value = coroutineScope.launch {
                delay(5000)
                showControls = false
            }
        }
    }

    SwipeDetector(
        onSwipeLeft = onSwipeLeft,
        onSwipeRight = onSwipeRight,
        modifier = modifier
    ) {
        ClickableArea(
            onClick = { showControls = !showControls },
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AudioFileIcon(
                        modifier = Modifier.padding(top = 32.dp),
                        size = 120.dp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.audio_file),
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                AnimatedVisibility(
                    visible = showControls,
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    MediaControls(
                        modifier = Modifier.align(Alignment.Center),
                        isPlaying = isPlaying,
                        onPlayPause = onPlayPause,
                        onSeekBackward = onSeekBackward,
                        onSeekForward = onSeekForward,
                        controlsSize = MediaControlsSize.MEDIUM
                    )
                }

                AnimatedVisibility(
                    visible = showControls,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 84.dp)
                ) {
                    GradientBackground(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        MediaProgressBar(
                            progress = progress,
                            currentPosition = currentPosition,
                            duration = duration,
                            onSeek = onSeek,
                            formatTime = formatTime
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MediaThumbnail(
    uiMediaItem: UIMediaItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
    ) {
        when (uiMediaItem.type) {
            MediaType.IMAGE -> {
                AsyncImage(
                    model = uiMediaItem.url,
                    contentDescription = stringResource(R.string.media_thumbnail),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            MediaType.VIDEO -> {
                AsyncImage(
                    model = uiMediaItem.thumbnailUrl ?: uiMediaItem.url,
                    contentDescription = stringResource(R.string.video_thumbnail),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Icon(
                    painter = painterResource(R.drawable.play_arrow),
                    contentDescription = stringResource(R.string.video),
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            CircleShape
                        )
                        .padding(4.dp)
                        .size(16.dp)
                )
            }

            MediaType.AUDIO -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.audio_file),
                        contentDescription = stringResource(R.string.audio),
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}