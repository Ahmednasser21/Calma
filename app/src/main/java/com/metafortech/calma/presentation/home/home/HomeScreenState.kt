package com.metafortech.calma.presentation.home.home

data class HomeScreenState(
    val isLoading: Boolean = false,
    val userImageUrl: String = "",
    val userName: String = "",
    val posts: List<PostModel> = emptyList(),
    val error: String? = null,
)

data class PostModel(
    val id: String,
    val userAvatar: String,
    val userName: String,
    val timeAgo: String,
    val content: String,
    val mediaItems: List<MediaItem>,
    val hashtags: List<String>,
    val likesCount: Int,
    val commentsCount: Int,
    val sharesCount: Int,
    val isLiked: Boolean
)

data class MediaItem(
    val id: String,
    val type: MediaType,
    val url: String,
    val thumbnailUrl: String? = null,
    val duration: String? = null
)

enum class MediaType {
    IMAGE, VIDEO, AUDIO
}