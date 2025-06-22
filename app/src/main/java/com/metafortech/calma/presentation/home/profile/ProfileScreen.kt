package com.metafortech.calma.presentation.home.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.metafortech.calma.R
import com.metafortech.calma.presentation.ErrorStateIndicator
import com.metafortech.calma.presentation.LoadingStateIndicator
import com.metafortech.calma.presentation.home.home.HomeScreenState
import com.metafortech.calma.presentation.home.home.SocialMediaFeedItem
import com.metafortech.calma.presentation.home.home.UIMediaItem
import com.metafortech.calma.presentation.theme.Typography

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState,
    state: HomeScreenState,
    viewModel: ProfileViewModel = hiltViewModel(),
    onLikePost: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onSharePost: (String) -> Unit,
    onMediaClick: (List<UIMediaItem>, index: Int) -> Unit,
    onPostCreatorClick: () -> Unit,
    onPostOptionsMenuClick: () -> Unit,
    onPlayAudio: (String) -> Unit,
    onPauseAudio: () -> Unit,
    onSeekAudio: (Long) -> Unit,
    onHashtagClick: (String) -> Unit,
    formatTime: (Long) -> String,
    onShowMoreClicked: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        when {
            uiState.isLoading -> {
                LoadingStateIndicator(isLoading = true)
            }

            uiState.error != null -> {
                ErrorStateIndicator(error = uiState.error)
            }

            uiState.profileData != null -> {
                ProfileContent(
                    modifier = modifier,
                    profileData = uiState.profileData,
                    viewModel = viewModel,
                    state = state,
                    selectedTab = uiState.selectedTap,
                    onLikePost = onLikePost,
                    onCommentClick = onCommentClick,
                    onSharePost = onSharePost,
                    onMediaClick = onMediaClick,
                    onPostCreatorClick = onPostCreatorClick,
                    onPostOptionsMenuClick = onPostOptionsMenuClick,
                    onPlayAudio = onPlayAudio,
                    onPauseAudio = onPauseAudio,
                    onSeekAudio = onSeekAudio,
                    onHashtagClick = onHashtagClick,
                    formatTime = formatTime,
                    onShowMoreClicked = onShowMoreClicked,
                    listState = state.listState
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier,
    profileData: ProfileData,
    selectedTab: Int,
    state: HomeScreenState,
    onLikePost: (String) -> Unit,
    onCommentClick: (String) -> Unit,
    onSharePost: (String) -> Unit,
    onMediaClick: (List<UIMediaItem>, index: Int) -> Unit,
    onPostCreatorClick: () -> Unit,
    onPostOptionsMenuClick: () -> Unit,
    onPlayAudio: (String) -> Unit,
    onPauseAudio: () -> Unit,
    onSeekAudio: (Long) -> Unit,
    onHashtagClick: (String) -> Unit,
    formatTime: (Long) -> String,
    onShowMoreClicked: (String) -> Unit,
    listState: LazyListState,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState
    ) {
        item {
            ProfileHeader(
                onBackClick = viewModel::onBackClick,
                onShareClick = viewModel::onShareClick
            )
        }

        // Profile Image
        item {
            ProfileImage(
                imageUrl = profileData.profileImageUrl,
                countryFlagUrl = profileData.countryFlagUrl
            )
        }

        // Profile Info Section
        item {
            Column(
                modifier = Modifier
                    .offset(y = (-60).dp)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ProfileInfo(
                    name = profileData.name,
                    description = profileData.description,
                    sportsType = profileData.sportsType
                )

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    thickness = 8.dp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                ProfileActions(
                    friendsCount = profileData.friendsCount,
                    isOwnProfile = profileData.isOwnProfile,
                    onEditProfileClick = viewModel::onEditProfileClick
                )

                Spacer(modifier = Modifier.height(32.dp))

                ProfileTabs(
                    selectedTab = selectedTab,
                    onTabSelected = viewModel::onTabSelected
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        when (selectedTab) {
            2 -> {
                items(
                    items = state.posts,
                    key = { post -> post.id }
                ) { post ->
                    SocialMediaFeedItem(
                        modifier = Modifier.offset(y = (-60).dp),
                        post = post,
                        audioPlayerState = state.audioPlayerState,
                        onCommentClick = onCommentClick,
                        onMediaClick = onMediaClick,
                        onPostCreatorClick = onPostCreatorClick,
                        onPostOptionsMenuClick = onPostOptionsMenuClick,
                        onPlayAudio = onPlayAudio,
                        onPauseAudio = onPauseAudio,
                        onSeekAudio = onSeekAudio,
                        onHashtagClick = onHashtagClick,
                        formatTime = formatTime,
                        onShowMoreClicked = onShowMoreClicked,
                        onLikeClick = onLikePost,
                        onShareClick = onSharePost,
                    )
                }
            }

            1 -> {

            }

            else -> {

            }
        }
    }
}


@Composable
private fun ProfileHeader(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.stadium),
            contentDescription = "Stadium background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.background
                )
            }

            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = stringResource(R.string.share),
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

@Composable
private fun ProfileImage(
    imageUrl: String,
    countryFlagUrl: String,
) {
    Box(
        modifier = Modifier.offset(y = (-60).dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.profile_image),
            modifier = Modifier
                .size(130.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .border(4.dp, Color.Black, CircleShape),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                model = countryFlagUrl,
                contentDescription = "Country Flag"
            )
        }
    }
}

@Composable
private fun ProfileInfo(
    name: String,
    description: String,
    sportsType: String,
) {
    Text(
        modifier = Modifier.padding(top = 12.dp),
        text = name,
        style = Typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center
    )
    Text(
        modifier = Modifier
            .width(250.dp)
            .padding(vertical = 8.dp),
        text = description,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp),
            text = sportsType,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "⚽",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ProfileActions(
    friendsCount: Int,
    isOwnProfile: Boolean,
    onEditProfileClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {},
            modifier = Modifier
                .height(48.dp)
                .padding(horizontal = 8.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
            shape = RoundedCornerShape(10.dp),
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .padding(horizontal = 4.dp),
                painter = painterResource(R.drawable.friend),
                contentDescription = stringResource(R.string.friends),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "$friendsCount " + stringResource(R.string.friend),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Button(
            onClick = onEditProfileClick,
            modifier = Modifier
                .height(48.dp)
                .padding(horizontal = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isOwnProfile) Color.Black else MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(10.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .padding(horizontal = 4.dp),
                imageVector = if (isOwnProfile) Icons.Default.Edit else Icons.Default.Add,
                contentDescription = if (isOwnProfile) stringResource(R.string.edit_profile) else stringResource(
                    R.string.follow
                ),
                tint = MaterialTheme.colorScheme.background,
            )
            Text(
                text = if (isOwnProfile) stringResource(R.string.edit_profile) else stringResource(R.string.follow),
                color = MaterialTheme.colorScheme.background,
                style = MaterialTheme.typography.bodySmall,
            )

        }
    }
}

@Composable
private fun ProfileTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    val tabs = listOf(
        Pair(stringResource(R.string.lives), R.drawable.live),
        Pair(stringResource(R.string.edifices), R.drawable.groups),
        Pair(stringResource(R.string.posts), R.drawable.gallery)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, (title, painterRes) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.clickable {
                            onTabSelected(index)
                        }
                    ) {
                        Icon(
                            painter = painterResource(painterRes),
                            contentDescription = title,
                            tint = if (index == selectedTab) Color.Red else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = title,
                            fontSize = 14.sp,
                            color = if (index == selectedTab) Color.Red else Color.Gray
                        )
                    }

                    if (index == selectedTab) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(4.dp)
                                .background(Color.Red, RoundedCornerShape(4.dp))
                        )
                    }
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            color = Color.Gray,
            thickness = 1.dp

        )
    }
}