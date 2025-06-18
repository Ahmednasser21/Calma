package com.metafortech.calma.presentation.home.creat

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.metafortech.calma.R
import com.metafortech.calma.presentation.ErrorStateIndicator
import com.metafortech.calma.presentation.LoadingIndicator
import com.metafortech.calma.presentation.home.media.MediaType

@Composable
fun NewPostScreen(
    modifier: Modifier = Modifier,
    uiState: NewPostUiState,
    addMediaFile: (MediaFile) -> Unit,
    addVideoWithThumbnail: (Uri) -> Unit,
    createPost: () -> Unit,
    updatePostText: (String) -> Unit,
    removeMediaFile: (MediaFile) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { addMediaFile(MediaFile(it, MediaType.IMAGE)) }
    }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { addVideoWithThumbnail(it) }
    }

    val audioPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { addMediaFile(MediaFile(it, MediaType.AUDIO)) }
    }

    LaunchedEffect(uiState.isPostSuccessful) {
        if (uiState.isPostSuccessful) {
            onNavigateBack()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        PostTopBar(
            onBackClick = onNavigateBack,
            onPostClick = { createPost() },
            isLoading = uiState.isLoading,
            canPost = uiState.postText.isNotBlank() || uiState.mediaFiles.isNotEmpty()
        )

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                UserProfileSection(
                    userImageUrl = uiState.userImageUrl,
                    userName = uiState.userName
                )

                Spacer(modifier = Modifier.height(24.dp))

                PostTextInput(
                    text = uiState.postText,
                    onTextChange = { newValue ->
                        updatePostText(newValue)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                if (uiState.mediaFiles.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))

                    MediaFilesSection(
                        mediaFiles = uiState.mediaFiles,
                        onRemoveFile = { mediaFile ->
                            removeMediaFile(mediaFile)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(24.dp))

                MediaSelectionSection(
                    onImageClick = { imagePicker.launch("image/*") },
                    onVideoClick = { videoPicker.launch("video/*") },
                    onAudioClick = { audioPicker.launch("audio/*") }
                )

                uiState.error?.let { error ->
                    ErrorStateIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        error = error
                    )
                }
            }
        }
    }
}

@Composable
private fun PostTopBar(
    onBackClick: () -> Unit,
    onPostClick: () -> Unit,
    isLoading: Boolean,
    canPost: Boolean,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onPostClick,
                enabled = canPost && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier.height(50.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.post),
                        color = if (canPost) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = stringResource(R.string.create_new_post),
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                color = MaterialTheme.colorScheme.primary
            )

            Surface(
                onClick = onBackClick,
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.background,
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun UserProfileSection(
    modifier: Modifier = Modifier,
    userImageUrl: String,
    userName: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = modifier.size(48.dp),
            shape = CircleShape,
            color = Color(0xFFE3F2FD),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.profile_image),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = userName,
            fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun PostTextInput(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.Gray.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { newValue ->
                onTextChange(newValue)
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.what_is_new_in_your_sports_Journey),
                    color = Color.Gray,
                    fontStyle = MaterialTheme.typography.bodySmall.fontStyle
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .defaultMinSize(minHeight = 120.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                lineHeight = 24.sp
            )
        )
    }
}

@Composable
private fun MediaFilesSection(
    mediaFiles: List<MediaFile>,
    onRemoveFile: (MediaFile) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.attach_file),
                contentDescription = null,
                tint = Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.attached_files) + " (${mediaFiles.size})",
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mediaFiles) { mediaFile ->
                MediaFileItem(
                    mediaFile = mediaFile,
                    onRemove = { onRemoveFile(mediaFile) }
                )
            }
        }
    }
}

@Composable
private fun MediaFileItem(
    mediaFile: MediaFile,
    onRemove: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 4.dp
        ) {
            when (mediaFile.type) {
                MediaType.IMAGE -> {
                    AsyncImage(
                        model = mediaFile.uri,
                        contentDescription = stringResource(R.string.image),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                MediaType.VIDEO -> {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (mediaFile.thumbnailUri != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(mediaFile.thumbnailUri)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = stringResource(R.string.video_preview),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.onBackground),
                                contentAlignment = Alignment.Center
                            ) {
                                LoadingIndicator(modifier = Modifier.size(24.dp))
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxSize(),
                            color = Color.Black.copy(alpha = 0.3f)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = stringResource(R.string.video),
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }

                        mediaFile.duration?.let { duration ->
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(6.dp),
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            ) {
                                Text(
                                    text = duration.toString(),
                                    color = MaterialTheme.colorScheme.background,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                MediaType.AUDIO -> {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFF8BC34A)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.audio_file),
                                    contentDescription = stringResource(R.string.audio),
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )

                                mediaFile.duration?.let { duration ->
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = duration.toString(),
                                        color = Color.White,
                                        fontStyle = MaterialTheme.typography.bodySmall.fontStyle
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Surface(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 6.dp, y = (-6).dp)
                .size(28.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.onBackground,
            shadowElevation = 4.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.remove),
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun MediaSelectionSection(
    onImageClick: () -> Unit,
    onVideoClick: () -> Unit,
    onAudioClick: () -> Unit,
) {
    Column {
        Text(
            text = stringResource(R.string.add_media),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MediaOption(
                icon = painterResource(R.drawable.gallery),
                label = stringResource(R.string.image),
                color = Color(0xFF4CAF50),
                onClick = onImageClick
            )

            MediaOption(
                icon = painterResource(R.drawable.live_gray),
                label = stringResource(R.string.video),
                color = Color(0xFF2196F3),
                onClick = onVideoClick
            )

            MediaOption(
                icon = painterResource(R.drawable.audio_file),
                label = stringResource(R.string.audio),
                color = Color(0xFFFF9800),
                onClick = onAudioClick
            )
        }
    }
}

@Composable
private fun MediaOption(
    icon: Painter,
    label: String,
    color: Color,
    onClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = color.copy(alpha = 0.12f),
            border = BorderStroke(2.dp, color.copy(alpha = 0.3f)),
            shadowElevation = 2.dp
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = label,
            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
            color = Color.Gray
        )
    }
}