package com.metafortech.calma.presentation.home.home

data class HomeScreenState (
    val isLoading: Boolean = false,
    val userImageUrl: String = "",
    val userName: String = "",
    val posts: List<Post> = emptyList(),
    val error: String? = null
)
data class Post(
    val isLiked: Boolean,
)
