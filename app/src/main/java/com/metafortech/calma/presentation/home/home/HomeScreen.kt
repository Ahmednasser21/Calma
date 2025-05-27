package com.metafortech.calma.presentation.home.home

import android.R.attr.progress
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onCreateNewPostClick: () -> Unit = {},
    onLikePost: (String) -> Unit = {},
    onCommentPost: (String) -> Unit = {},
    onSharePost: (String) -> Unit = {},
    onMediaClick: (MediaItem) -> Unit = {},
    onPostCreatorClick: () -> Unit = {},
    onPostOptionsMenuClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        CreateNewPost(
            onCreateNewPostClick = { onCreateNewPostClick() },
            state = state
        )
        SocialMediaFeed(
            posts = state.posts,
            onLikePost = onLikePost,
            onCommentPost = onCommentPost,
            onSharePost = onSharePost,
            onMediaClick = onMediaClick,
            onPostCreatorClick = onPostCreatorClick,
            onPostOptionsMenuClick = { onPostOptionsMenuClick() }
        )
    }
}

@Composable
fun CreateNewPost(
    onCreateNewPostClick: () -> Unit = {},
    state: HomeScreenState
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
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
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
                    CreatePostOptionsItem(
                        painter = painterResource(id = R.drawable.live_gray),
                        stringResource(R.string.record_video)
                    )
                    CreatePostOptionsItem(
                        painter = painterResource(id = R.drawable.camera),
                        stringResource(R.string.take_photo)
                    )
                    CreatePostOptionsItem(
                        painter = painterResource(id = R.drawable.voice_mail),
                        stringResource(R.string.record_audio)
                    )
                    CreatePostOptionsItem(
                        painter = painterResource(id = R.drawable.gallery),
                        stringResource(R.string.select_from_gallery)
                    )

                }
            }
        }
    }
}

@Composable
fun CreatePostOptionsItem(painter: Painter, description: String) {
    Image(
        modifier = Modifier.size(18.dp),
        painter = painter,
        contentDescription = description
    )
}

@Composable
fun SocialMediaFeed(
    posts: List<PostModel>,
    onLikePost: (String) -> Unit = {},
    onCommentPost: (String) -> Unit = {},
    onSharePost: (String) -> Unit = {},
    onMediaClick: (MediaItem) -> Unit = {},
    onPostCreatorClick: () -> Unit,
    onPostOptionsMenuClick: () -> Unit,
    onHashtagClick: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(
            items = posts,
            key = { it.id }
        ) { post ->
            SocialMediaFeedItem(
                post = post,
                onLikeClick = onLikePost,
                onCommentClick = onCommentPost,
                onShareClick = onSharePost,
                onMediaClick = onMediaClick,
                onPostCreatorClick = onPostCreatorClick,
                onPostOptionsMenuClick = onPostOptionsMenuClick,
                onHashtagClick = onHashtagClick

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
    onMediaClick: (MediaItem) -> Unit,
    onPostCreatorClick: () -> Unit,
    onPostOptionsMenuClick: () -> Unit,
    onHashtagClick: (String) -> Unit
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
                onPostOptionsMenuClick = { onPostOptionsMenuClick() }
            )

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

            if (post.mediaItems.isNotEmpty()) {
                MediaContent(
                    modifier = Modifier.fillMaxWidth(),
                    mediaItems = post.mediaItems,
                    onMediaClick = { mediaItem -> onMediaClick(mediaItem) }
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
                onShareClick = { onShareClick(post.id) }
            )
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
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
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
                text = timeAgo,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.more),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(24.dp)
                .clickable { onPostOptionsMenuClick() }
        )

    }
}

@Composable
private fun MediaContent(
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    when (mediaItems.size) {
        1 -> SingleMediaItem(
            mediaItem = mediaItems[0],
            onMediaClick = onMediaClick,
            modifier = modifier
        )

        2 -> TwoMediaItems(
            mediaItems = mediaItems,
            onMediaClick = onMediaClick,
            modifier = modifier
        )

        3 -> ThreeMediaItems(
            mediaItems = mediaItems,
            onMediaClick = onMediaClick,
            modifier = modifier
        )

        else -> MultipleMediaItems(
            mediaItems = mediaItems,
            onMediaClick = onMediaClick,
            modifier = modifier
        )
    }
}

@Composable
private fun SingleMediaItem(
    mediaItem: MediaItem,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    when (mediaItem.type) {
        MediaType.IMAGE -> {
            ImageLoading(
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onMediaClick(mediaItem) },
                imageURL = mediaItem.url,
                contentDescription = stringResource(R.string.post_image)
            )
        }

        MediaType.VIDEO -> {
            VideoThumbnail(
                videoUrl = mediaItem.url,
                thumbnailUrl = mediaItem.thumbnailUrl,
                duration = mediaItem.duration,
                onVideoClick = { onMediaClick(mediaItem) },
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        MediaType.AUDIO -> {
            AudioPlayer(
                audioUrl = mediaItem.url,
                duration = mediaItem.duration,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun TwoMediaItems(
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        mediaItems.take(2).forEach { mediaItem ->
            MediaItemThumbnail(
                mediaItem = mediaItem,
                onMediaClick = onMediaClick,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun ThreeMediaItems(
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MediaItemThumbnail(
            mediaItem = mediaItems[0],
            onMediaClick = onMediaClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MediaItemThumbnail(
                mediaItem = mediaItems[1],
                onMediaClick = onMediaClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            MediaItemThumbnail(
                mediaItem = mediaItems[2],
                onMediaClick = onMediaClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun MultipleMediaItems(
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        MediaItemThumbnail(
            mediaItem = mediaItems[0],
            onMediaClick = onMediaClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MediaItemThumbnail(
                mediaItem = mediaItems[1],
                onMediaClick = onMediaClick,
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
                    mediaItem = mediaItems[2],
                    onMediaClick = onMediaClick,
                    modifier = Modifier.fillMaxSize()
                )

                if (mediaItems.size > 3) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Color.Black.copy(alpha = 0.6f),
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onMediaClick(mediaItems[2]) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${mediaItems.size - 3}",
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
    mediaItem: MediaItem,
    onMediaClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onMediaClick(mediaItem) }
    ) {
        when (mediaItem.type) {
            MediaType.IMAGE -> {
                ImageLoading(
                    modifier = Modifier.fillMaxSize(),
                    imageURL = mediaItem.url,
                    contentDescription = stringResource(R.string.media_thumbnail)
                )
            }

            MediaType.VIDEO -> {
                ImageLoading(
                    modifier = Modifier.fillMaxSize(),
                    imageURL = mediaItem.thumbnailUrl ?: mediaItem.url,
                    contentDescription = stringResource(R.string.video_thumbnail),
                )

                Icon(
                    imageVector = Icons.Default.PlayArrow,
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
                        imageVector = Icons.Default.PlayArrow,
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
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onVideoClick() }
    ) {
        ImageLoading(
            modifier = Modifier.fillMaxSize(),
            imageURL = thumbnailUrl ?: videoUrl,
            contentDescription = stringResource(R.string.video_thumbnail)
        )

        Icon(
            imageVector = Icons.Default.PlayArrow,
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
                        Color.Black.copy(alpha = 0.6f),
                        RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun AudioPlayer(
    audioUrl: String,
    duration: String?,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableFloatStateOf(0f) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { isPlaying = !isPlaying }
            ) {
                Icon(
                    painter = if (isPlaying) painterResource(id = R.drawable.pause)
                    else painterResource(
                        id = R.drawable.play_arrow
                    ),
                    contentDescription = if (isPlaying) stringResource(R.string.pause) else stringResource(
                        R.string.play
                    ),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Modifier.fillMaxWidth()
                LinearProgressIndicator(
                    progress = { progress.toFloat() },
                    modifier = modifier,
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "0:00",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        text = duration ?: "0:00",
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

@Composable
private fun HashtagsRow(
    hashtags: List<String>,
    modifier: Modifier = Modifier,
    onHashtagClick: (String) -> Unit
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(hashtags) { hashtag ->
            Text(
                text = hashtag,
                color = Color(0xFF1DA1F2),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onHashtagClick(hashtag) }
            )
        }
    }
}

@Composable
private fun EngagementStats(
    likesCount: Int,
    commentsCount: Int,
    sharesCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
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
    icon: Painter,
    text: String,
    tint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(
                    bounded = false,
                    radius = 32.dp,
                    color = MaterialTheme.colorScheme.secondary
                ),
            ) { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = tint,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
