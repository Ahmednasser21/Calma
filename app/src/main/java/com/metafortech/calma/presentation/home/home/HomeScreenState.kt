package com.metafortech.calma.presentation.home.home

import androidx.compose.foundation.lazy.LazyListState
import kotlinx.serialization.Serializable

data class HomeScreenState(
    val isLoading: Boolean = false,
    val userImageUrl: String = "",
    val userName: String = "",
    val listState: LazyListState = LazyListState(),
    val posts: List<PostModel> = emptyList(),
    val error: String? = null,
    val audioPlayerState: AudioPlayerState = AudioPlayerState(),
    val showComments: Boolean = false,
    val commentPostId:String = ""
)
data class AudioPlayerState(
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val progress: Float = 0f,
    val currentAudioUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
data class PostModel(
    val id: String,
    val userAvatar: String,
    val userName: String,
    val timeAgo: String,
    val content: String,
    val uiMediaItems: List<UIMediaItem>,
    val hashtags: List<String>,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean,
    val isShowMoreClicked: Boolean = false,
    val comments: List<Comment> = emptyList(),
    val newCommentText: String = "",
    val commentSubmitting: Boolean = false,
    val commentError: String? = null
)
@Serializable
data class UIMediaItem(
    val id: String,
    val type: MediaType,
    val url: String,
    val thumbnailUrl: String? = null,
    val duration: String? = null
)

enum class MediaType {
    IMAGE, VIDEO, AUDIO
}
data class Comment(
    val id: String,
    val postId: String,
    val authorName: String,
    val authorAvatar: String? = null,
    val content: String,
    val timestamp: Long,
    val isOwnComment: Boolean = false
)