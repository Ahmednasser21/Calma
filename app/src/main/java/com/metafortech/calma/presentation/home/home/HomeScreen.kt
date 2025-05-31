package com.metafortech.calma.presentation.home.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metafortech.calma.R
import com.metafortech.calma.presentation.ImageLoading
import com.metafortech.calma.presentation.UserCircularImage
import kotlin.collections.List

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    onCreateNewPostClick: () -> Unit,
    onLikePost: (String) -> Unit,
    onCommentPost: (String) -> Unit,
    onSharePost: (String) -> Unit,
    onMediaClick: (List<UIMediaItem>, index: Int) -> Unit,
    onPostCreatorClick: () -> Unit,
    onPostOptionsMenuClick: () -> Unit,
    onPlayAudio: (String) -> Unit,
    onPauseAudio: () -> Unit,
    onSeekAudio: (Long) -> Unit,
    onHashtagClick: (String) -> Unit,
    onScroll: (List<Int>) -> Unit,
    formatTime: (Long) -> String,
) {
    val listState = state.listState
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        LaunchedEffect(listState) {
            snapshotFlow {
                listState.layoutInfo.visibleItemsInfo.map { it.index }
            }.collect { visibleIndices ->
                onScroll(visibleIndices)
            }
        }

        NewPost(
            onCreateNewPostClick = { onCreateNewPostClick() }, state = state
        )

        SocialMediaFeed(
            posts = state.posts,
            audioPlayerState = state.audioPlayerState,
            listState = listState,
            onLikePost = { postID ->
                onLikePost(postID)
            },
            onCommentPost = { postID ->
                onCommentPost(postID)
            },
            onSharePost = { postID ->
                onSharePost(postID)
            },
            onMediaClick = { mediaItems, index ->
                onMediaClick(mediaItems, index)
            },
            onPostCreatorClick = { onPostCreatorClick() },
            onPostOptionsMenuClick = { onPostOptionsMenuClick() },
            onPlayAudio = { audioUrl ->
                onPlayAudio(audioUrl)
            },
            onPauseAudio = { onPauseAudio() },
            onSeekAudio = { seekTime ->
                onSeekAudio(seekTime)
            },
            onHashtagClick = { hashtag ->
                onHashtagClick(hashtag)
            },
            formatTime = { timeLong ->
                formatTime(timeLong)
            }
        )
    }
}

@Composable
fun NewPost(
    onCreateNewPostClick: () -> Unit = {}, state: HomeScreenState,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UserCircularImage(imageUrl = state.userImageUrl)
        Column(
            horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = state.userName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier.clickable(onClick = { onCreateNewPostClick() })
            ) {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = stringResource(R.string.what_is_new_in_your_sports_Journey),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0x65000000)
                )
                Row(
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PostOptionsItem(
                        painter = painterResource(id = R.drawable.live_gray),
                        stringResource(R.string.record_video)
                    )
                    PostOptionsItem(
                        painter = painterResource(id = R.drawable.camera),
                        stringResource(R.string.take_photo)
                    )
                    PostOptionsItem(
                        painter = painterResource(id = R.drawable.voice_mail),
                        stringResource(R.string.record_audio)
                    )
                    PostOptionsItem(
                        painter = painterResource(id = R.drawable.gallery),
                        stringResource(R.string.select_from_gallery)
                    )

                }
            }
        }
    }
}

@Composable
fun PostOptionsItem(painter: Painter, description: String) {
    Image(
        modifier = Modifier.size(18.dp), painter = painter, contentDescription = description
    )
}

@Composable
fun SocialMediaFeed(
    posts: List<PostModel>,
    audioPlayerState: AudioPlayerState,
    listState: LazyListState,
    onLikePost: (String) -> Unit,
    onCommentPost: (String) -> Unit,
    onSharePost: (String) -> Unit,
    onMediaClick: (List<UIMediaItem>, index: Int) -> Unit,
    onPostCreatorClick: () -> Unit,
    onPostOptionsMenuClick: () -> Unit,
    onHashtagClick: (String) -> Unit,
    onPlayAudio: (String) -> Unit,
    onPauseAudio: () -> Unit,
    onSeekAudio: (Long) -> Unit,
    formatTime: (Long) -> String,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        items(
            items = posts, key = { it.id }) { post ->
            SocialMediaFeedItem(
                post = post,
                audioPlayerState = audioPlayerState,
                onLikeClick = { postID ->
                    onLikePost(postID)
                },
                onCommentClick = { postID ->
                    onCommentPost(postID)
                },
                onShareClick = { postID ->
                    onSharePost(postID)
                },
                onMediaClick = { mediaItems, index ->
                    onMediaClick(mediaItems, index)
                },
                onPostCreatorClick = { onPostCreatorClick() },
                onPostOptionsMenuClick = { onPostOptionsMenuClick() },
                onPlayAudio = { audioUrl ->
                    onPlayAudio(audioUrl)
                },
                onPauseAudio = { onPauseAudio() },
                onSeekAudio = { seekTime ->
                    onSeekAudio(seekTime)
                },
                onHashtagClick = { hashtag ->
                    onHashtagClick(hashtag)
                },
                formatTime = { timeLong ->
                    formatTime(timeLong)
                }
            )

        }
    }
}

@Composable
fun SocialMediaFeedItem(
    modifier: Modifier = Modifier,
    post: PostModel,
    onLikeClick: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onShareClick: (String) -> Unit,
    onMediaClick: (List<UIMediaItem>, index: Int) -> Unit,
    onPostCreatorClick: () -> Unit,
    onPostOptionsMenuClick: () -> Unit,
    onHashtagClick: (String) -> Unit,
    onPlayAudio: (String) -> Unit,
    onPauseAudio: () -> Unit,
    onSeekAudio: (Long) -> Unit,
    audioPlayerState: AudioPlayerState,
    formatTime: (Long) -> String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            PostHeader(
                modifier = Modifier.fillMaxWidth(),
                userAvatar = post.userAvatar,
                userName = post.userName,
                timeAgo = post.timeAgo,
                onPostCreatorClick = { onPostCreatorClick() },
                onPostOptionsMenuClick = { onPostOptionsMenuClick() })

            Spacer(modifier = Modifier.height(12.dp))

            if (post.content.isNotEmpty()) {
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (post.uiMediaItems.isNotEmpty()) {
                MediaContent(
                    modifier = Modifier.fillMaxWidth(),
                    uiMediaItems = post.uiMediaItems,
                    onMediaClick = { mediaItems, index -> onMediaClick(mediaItems, index) },
                    audioPlayerState = audioPlayerState,
                    onPlayAudio = { audioUrl ->
                        onPlayAudio(audioUrl)
                    },
                    onPauseAudio = { onPauseAudio() },
                    onSeekAudio = { seekTime ->
                        onSeekAudio(seekTime)
                    },
                    formatTime = { timeLong ->
                        formatTime(timeLong)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (post.hashtags.isNotEmpty()) {
                HashtagsRow(hashtags = post.hashtags, onHashtagClick = { hashtag ->
                    onHashtagClick(hashtag)
                })
                Spacer(modifier = Modifier.height(12.dp))
            }

            EngagementStats(
                likesCount = post.likesCount,
                commentsCount = post.commentsCount,
                sharesCount = post.sharesCount
            )

            Spacer(modifier = Modifier.height(8.dp))

            ActionButtons(
                isLiked = post.isLiked,
                onLikeClick = { onLikeClick(post.id) },
                onCommentClick = { onCommentClick(post.id) },
                onShareClick = { onShareClick(post.id) })
        }
    }
}

@Composable
private fun PostHeader(
    modifier: Modifier = Modifier,
    userAvatar: String,
    userName: String,
    timeAgo: String,
    onPostCreatorClick: () -> Unit,
    onPostOptionsMenuClick: () -> Unit,
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        UserCircularImage(imageUrl = userAvatar, onPostCreatorClick)
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.clickable { onPostCreatorClick() },
                text = userName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = timeAgo, style = MaterialTheme.typography.bodySmall, color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.more),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(24.dp)
                .clickable { onPostOptionsMenuClick() })

    }
}

@Composable
private fun MediaContent(
    modifier: Modifier = Modifier,
    uiMediaItems: List<UIMediaItem>,
    onMediaClick: (List<UIMediaItem>, Int) -> Unit,
    audioPlayerState: AudioPlayerState,
    onPlayAudio: (String) -> Unit,
    onPauseAudio: () -> Unit,
    onSeekAudio: (Long) -> Unit,
    formatTime: (Long) -> String,
) {
    when (uiMediaItems.size) {
        1 -> SingleMediaItem(
            modifier = modifier,
            uiMediaItem = uiMediaItems[0],
            onMediaClick = onMediaClick,
            audioPlayerState = audioPlayerState,
            onPlayAudio = onPlayAudio,
            onPauseAudio = onPauseAudio,
            onSeekAudio = onSeekAudio,
            formatTime = formatTime
        )


        2 -> TwoMediaItems(
            modifier = modifier,
            uiMediaItems = uiMediaItems,
            onMediaClick = onMediaClick
        )

        3 -> ThreeMediaItems(
            modifier = modifier,
            uiMediaItems = uiMediaItems,
            onMediaClick = onMediaClick
        )

        else -> MultipleMediaItems(
            modifier = modifier,
            uiMediaItems = uiMediaItems,
            onMediaClick = onMediaClick
        )
    }
}


@Composable
private fun SingleMediaItem(
    modifier: Modifier = Modifier,
    uiMediaItem: UIMediaItem,
    onMediaClick: (List<UIMediaItem>, index: Int) -> Unit,
    audioPlayerState: AudioPlayerState,
    onPlayAudio: (String) -> Unit,
    onPauseAudio: () -> Unit,
    onSeekAudio: (Long) -> Unit,
    formatTime: (Long) -> String,
) {
    when (uiMediaItem.type) {
        MediaType.IMAGE -> {
            ImageLoading(
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onMediaClick(listOf(uiMediaItem), 0) },
                imageURL = uiMediaItem.url,
                contentDescription = stringResource(R.string.post_image)
            )
        }

        MediaType.VIDEO -> {
            VideoThumbnail(
                videoUrl = uiMediaItem.url,
                thumbnailUrl = uiMediaItem.thumbnailUrl,
                duration = uiMediaItem.duration,
                onVideoClick = { onMediaClick(listOf(uiMediaItem), 0) },
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        MediaType.AUDIO -> {
            AudioPlayer(
                modifier = modifier.fillMaxWidth(),
                audioUrl = uiMediaItem.url,
                duration = uiMediaItem.duration,
                audioPlayerState = audioPlayerState,
                onPlayClick = { audioUrl ->
                    onPlayAudio(audioUrl)
                },
                onPauseClick = { onPauseAudio() },
                onSeek = { seekTime ->
                    onSeekAudio(seekTime)
                },
                formatTime = { timeLong ->
                    formatTime(timeLong)
                }

            )
        }
    }
}

@Composable
private fun TwoMediaItems(
    uiMediaItems: List<UIMediaItem>,
    onMediaClick: (List<UIMediaItem>, index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        uiMediaItems.take(2).forEachIndexed { index, mediaItem ->
            MediaItemThumbnail(
                uiMediaItem = mediaItem,
                onMediaClick = { onMediaClick(uiMediaItems, index) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun ThreeMediaItems(
    uiMediaItems: List<UIMediaItem>,
    onMediaClick: (List<UIMediaItem>, index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MediaItemThumbnail(
            uiMediaItem = uiMediaItems[0],
            onMediaClick = { onMediaClick(uiMediaItems, 0) },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MediaItemThumbnail(
                uiMediaItem = uiMediaItems[1],
                onMediaClick = { onMediaClick(uiMediaItems, 1) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            MediaItemThumbnail(
                uiMediaItem = uiMediaItems[2],
                onMediaClick = { onMediaClick(uiMediaItems, 2) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun MultipleMediaItems(
    uiMediaItems: List<UIMediaItem>,
    onMediaClick: (List<UIMediaItem>, index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MediaItemThumbnail(
            uiMediaItem = uiMediaItems[0],
            onMediaClick = { onMediaClick(uiMediaItems, 0) },
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MediaItemThumbnail(
                uiMediaItem = uiMediaItems[1],
                onMediaClick = { onMediaClick(uiMediaItems, 1) },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                MediaItemThumbnail(
                    uiMediaItem = uiMediaItems[2],
                    onMediaClick = { onMediaClick(uiMediaItems, 2) },
                    modifier = Modifier.fillMaxSize()
                )

                if (uiMediaItems.size > 3) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .clickable {
                                onMediaClick(
                                    uiMediaItems,
                                    2
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${uiMediaItems.size - 3}",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaItemThumbnail(
    uiMediaItem: UIMediaItem, onMediaClick: (UIMediaItem) -> Unit, modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onMediaClick(uiMediaItem) }) {
        when (uiMediaItem.type) {
            MediaType.IMAGE -> {
                ImageLoading(
                    modifier = Modifier.fillMaxSize(),
                    imageURL = uiMediaItem.url,
                    contentDescription = stringResource(R.string.media_thumbnail)
                )
            }

            MediaType.VIDEO -> {
                ImageLoading(
                    modifier = Modifier.fillMaxSize(),
                    imageURL = uiMediaItem.thumbnailUrl ?: uiMediaItem.url,
                    contentDescription = stringResource(R.string.video_thumbnail),
                )

                Icon(
                    painter = painterResource(R.drawable.play_arrow),
                    contentDescription = stringResource(R.string.play_video),
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            CircleShape
                        )
                        .padding(8.dp)
                )
            }

            MediaType.AUDIO -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.play_arrow),
                        contentDescription = stringResource(R.string.audio_file),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun VideoThumbnail(
    videoUrl: String,
    thumbnailUrl: String?,
    duration: String?,
    onVideoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onVideoClick() }) {
        ImageLoading(
            modifier = Modifier.fillMaxSize(),
            imageURL = thumbnailUrl ?: videoUrl,
            contentDescription = stringResource(R.string.video_thumbnail)
        )

        Icon(
            painter = painterResource(R.drawable.play_arrow),
            contentDescription = stringResource(R.string.play_video),
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
                .background(
                    Color.Black.copy(alpha = 0.6f),
                    CircleShape
                )
                .padding(12.dp)
        )
        duration?.let {
            Text(
                text = it,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(
                        Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun AudioPlayer(
    modifier: Modifier = Modifier,
    audioUrl: String,
    duration: String?,
    audioPlayerState: AudioPlayerState,
    onPlayClick: (String) -> Unit,
    onPauseClick: () -> Unit,
    onSeek: (Long) -> Unit,
    formatTime: (Long) -> String,
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        ), shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (audioPlayerState.isPlaying && audioPlayerState.currentAudioUrl == audioUrl) {
                            onPauseClick()
                        } else {
                            onPlayClick(audioUrl)
                        }
                    }
                ) {
                    if (audioPlayerState.isLoading && audioPlayerState.currentAudioUrl == audioUrl) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Icon(
                            painter = if (audioPlayerState.isPlaying && audioPlayerState.currentAudioUrl == audioUrl) painterResource(
                                R.drawable.pause
                            ) else painterResource(R.drawable.play_arrow),
                            contentDescription = if (audioPlayerState.isPlaying && audioPlayerState.currentAudioUrl == audioUrl) stringResource(
                                R.string.pause
                            ) else stringResource(R.string.play),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    if (audioPlayerState.currentAudioUrl == audioUrl) {
                        Slider(
                            value = audioPlayerState.progress,
                            onValueChange = { progress ->
                                val newPosition = (progress * audioPlayerState.duration).toLong()
                                onSeek(newPosition)
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.secondary,
                                activeTrackColor = MaterialTheme.colorScheme.secondary,
                                inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            )
                        )
                    } else {
                        LinearProgressIndicator(
                            progress = { 0f },
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                            trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                            strokeCap = StrokeCap.Round
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (audioPlayerState.currentAudioUrl == audioUrl) formatTime(
                                audioPlayerState.currentPosition
                            ) else "0:00",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = if (audioPlayerState.currentAudioUrl == audioUrl) formatTime(
                                audioPlayerState.duration
                            ) else (duration ?: "0:00"),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                Icon(
                    painter = painterResource(R.drawable.mic),
                    contentDescription = "Audio",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun HashtagsRow(
    hashtags: List<String>, modifier: Modifier = Modifier, onHashtagClick: (String) -> Unit,
) {
    LazyRow(
        modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hashtags) { hashtag ->
            Text(
                text = hashtag,
                color = Color(0xFF1DA1F2),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onHashtagClick(hashtag) })
        }
    }
}

@Composable
private fun EngagementStats(
    likesCount: Int, commentsCount: Int, sharesCount: Int, modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$likesCount ${stringResource(R.string.like)}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )

        Row {
            Text(
                text = "$commentsCount ${stringResource(R.string.comment)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "$sharesCount ${stringResource(R.string.share)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun ActionButtons(
    isLiked: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ActionButton(
            icon = if (isLiked) painterResource(R.drawable.like_selected) else painterResource(
                R.drawable.like_not_selected
            ),
            text = stringResource(R.string.like_verb),
            tint = if (isLiked) Color.Red else Color.Gray,
            onClick = onLikeClick
        )

        ActionButton(
            icon = painterResource(R.drawable.comment),
            text = stringResource(R.string.comment),
            tint = Color.Gray,
            onClick = onCommentClick
        )

        ActionButton(
            icon = painterResource(R.drawable.share),
            text = stringResource(R.string.share),
            tint = Color.Gray,
            onClick = onShareClick
        )
    }
}


@Composable
private fun ActionButton(
    icon: Painter, text: String, tint: Color, onClick: () -> Unit, modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = false, radius = 32.dp, color = MaterialTheme.colorScheme.secondary
                ),
            ) { onClick() }
            .padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = icon, contentDescription = text, tint = tint, modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text, color = tint, style = MaterialTheme.typography.bodySmall
        )
    }
}