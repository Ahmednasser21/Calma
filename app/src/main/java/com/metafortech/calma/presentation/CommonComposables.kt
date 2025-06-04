package com.metafortech.calma.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.metafortech.calma.R
import com.metafortech.calma.presentation.home.media.MediaControlsSize
import kotlin.math.abs

@Composable
fun GeneralTextField(
    modifier: Modifier = Modifier,
    textValue: String,
    enabled: Boolean = true,
    placeHolder: String,
    label: String,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = textValue,
        onValueChange = { value ->
            onValueChange(value)
        },
        enabled = enabled,
        placeholder = {
            Text(
                text = placeHolder, style = MaterialTheme.typography.bodyMedium
            )
        },
        label = {
            Text(
                text = label, style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType, imeAction = imeAction
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = Color.Red,
            cursorColor = MaterialTheme.colorScheme.secondary,
            disabledLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier, password: String, onValueChange: (String) -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = password,
        onValueChange = { value ->
            onValueChange(value)
        },
        label = {
            Text(
                text = stringResource(
                    R.string.password_label
                ), style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
        ),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                Icon(
                    painter = if (isPasswordVisible) painterResource(id = R.drawable.baseline_visibility_24)
                    else painterResource(
                        id = R.drawable.baseline_visibility_off_24
                    ),
                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            errorBorderColor = Color.Red,
            cursorColor = MaterialTheme.colorScheme.secondary,
        )
    )
}

@Composable
fun LoginButton(content: String, onLoginClick: () -> Unit) {
    Button(
        onClick = onLoginClick,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(220.dp)
            .height(55.dp)
            .padding(bottom = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
        elevation = ButtonDefaults.buttonElevation(4.dp)

    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
fun TextButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() },
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun BottomPartOfLoginAndRegisterScreen(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    buttonText:String,
    text:String,
    textButtonText:String,
    onTextButtonClick: () -> Unit,
    onLoginWithGoogleClick: () -> Unit,
    onLoginWithFacebookClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginButton(buttonText) {
            onButtonClick()
        }
        Row(
            modifier = modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            TextButton(text = textButtonText) {
                onTextButtonClick()
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f), color = Color.LightGray

            )
            Text(
                text = stringResource(R.string.continue_using),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 4.dp),
                color = Color.DarkGray
            )

            HorizontalDivider(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f), color = Color.LightGray

            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GoogleOrFacebookButton(
                modifier = Modifier.weight(1f),
                painter = painterResource(id = R.drawable.google_logo),
                btnText = stringResource(R.string.google),
                btnTextColor = MaterialTheme.colorScheme.primary,
                btnColor = Color(0xFFECECEC)
            ) {
                onLoginWithGoogleClick()
            }

            GoogleOrFacebookButton(
                modifier = Modifier.weight(1f),
                painter = painterResource(id = R.drawable.facebook_logo),
                btnText = stringResource(R.string.facebook),
                btnTextColor = MaterialTheme.colorScheme.onPrimary,
                btnColor = Color(0xFF1877F2)
            ) {
                onLoginWithFacebookClick()
            }

        }
    }
}

@Composable
fun GoogleOrFacebookButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    btnText: String,
    btnTextColor: Color,
    btnColor: Color,
    onLoginClick: () -> Unit
) {
    Button(
        onClick = { onLoginClick() },
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(containerColor = btnColor),
        contentPadding = PaddingValues(horizontal = 16.dp),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .width(32.dp)
                .height(48.dp),
            tint = Color.Unspecified
        )
        Spacer(modifier = Modifier.width(if (btnText == stringResource(R.string.google)) 24.dp else 8.dp))
        Text(
            text = btnText, style = MaterialTheme.typography.bodyMedium, color = btnTextColor
        )
    }
}

@Composable
fun NextButton(modifier: Modifier = Modifier, enabled: Boolean = true, onNextClick: () -> Unit){
    Button(
        onClick = onNextClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal= 32.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        enabled = enabled
    ) {
        Text(
            text = stringResource(R.string.next),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun CircularProgressOnLoadingState(isLoading: Boolean,modifier: Modifier = Modifier) {
    if (isLoading) {
        Box(
            modifier = modifier.size(64.dp)
                .background(Color.Black.copy(alpha = 0.1f), shape = RoundedCornerShape(24.dp)),
        ) {
            CircularProgressIndicator(
                modifier = modifier
                    .size(48.dp)
                    .padding(8.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 2.dp
            )
        }
    }
}
@Composable
fun BackButton(modifier: Modifier = Modifier, onBackClick: () -> Unit){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.3f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
    }
}

@Composable
fun ImageLoading(
    imageURL: String?,
    contentDescription: String?,
    modifier: Modifier,
    onLoadingAdditionalBoolean: Boolean = false,
    onImageLoad: @Composable () -> Unit = {}
) {
    val painter = rememberAsyncImagePainter(imageURL)
    val state = painter.state.collectAsState().value

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state is AsyncImagePainter.State.Loading || onLoadingAdditionalBoolean ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                color = Color.Red
            )
        } else if (state is AsyncImagePainter.State.Success) {
            Image(
                painter = painter,
                modifier = modifier,
                contentDescription = contentDescription,
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.Center
            )
            onImageLoad()
        }
    }
}
@Composable
fun UserCircularImage(imageUrl: String,onUserClick: () -> Unit = {}){
    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.profile_image),
        modifier = Modifier
            .padding(start = 8.dp)
            .size(54.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onUserClick() },
        contentScale = ContentScale.FillBounds,
        error = painterResource(R.drawable.outline_person_24)
    )
}
@Composable
fun LoadingStateIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(modifier = modifier) {
        content()

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.loading),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ErrorStateIndicator(
    error: String?,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null
) {
    AnimatedVisibility(
        visible = !error.isNullOrEmpty(),
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_error_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = error ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.weight(1f)
                )
                if (onRetry != null) {
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = onRetry) {
                        Text(
                            text = stringResource(R.string.retry),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun MediaProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    currentPosition: Long,
    duration: Long,
    onSeek: (Long) -> Unit,
    formatTime : (Long) -> String
) {
    Column(modifier = modifier) {
        Slider(
            value = progress,
            onValueChange = { newProgress ->
                val newPosition = (newProgress * duration).toLong()
                onSeek(newPosition)
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPosition),
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = formatTime(duration),
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MediaControls(
    isPlaying: Boolean,
    onPlayPause: () -> Unit,
    onSeekBackward: () -> Unit,
    onSeekForward: () -> Unit,
    modifier: Modifier = Modifier,
    showBackwardForward: Boolean = true,
    controlsSize: MediaControlsSize = MediaControlsSize.MEDIUM
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            if (controlsSize == MediaControlsSize.LARGE) 32.dp else 16.dp
        ),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (showBackwardForward) {
            IconButton(
                onClick = onSeekBackward,
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.6f),
                        CircleShape
                    )
                    .size(controlsSize.backwardForwardSize)
            ) {
                Icon(
                    painter = painterResource(R.drawable.replay_10),
                    contentDescription = stringResource(R.string.replay_10),
                    tint = Color.White,
                    modifier = Modifier.size(controlsSize.iconSize)
                )
            }
        }

        IconButton(
            onClick = onPlayPause,
            modifier = Modifier
                .background(
                    Color.Black.copy(alpha = 0.6f),
                    CircleShape
                )
                .size(controlsSize.playPauseSize)
        ) {
            Icon(
                painter = if (isPlaying) painterResource(R.drawable.pause) else painterResource(R.drawable.play_arrow),
                contentDescription = if (isPlaying) stringResource(R.string.pause) else stringResource(R.string.play),
                tint = Color.White,
                modifier = Modifier.size(controlsSize.playIconSize)
            )
        }

        if (showBackwardForward) {
            IconButton(
                onClick = onSeekForward,
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.6f),
                        CircleShape
                    )
                    .size(controlsSize.backwardForwardSize)
            ) {
                Icon(
                    painter = painterResource(R.drawable.forward_10),
                    contentDescription = stringResource(R.string.forward_10),
                    tint = Color.White,
                    modifier = Modifier.size(controlsSize.iconSize)
                )
            }
        }
    }
}


@Composable
fun SwipeDetector(
    modifier: Modifier = Modifier,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    enabled: Boolean = true,
    swipeThreshold: Float = 50f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.pointerInput(enabled) {
            if (enabled) {
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

                    if (!hasNavigated && abs(totalDragX) > abs(dragAmount.y * 2)) {
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
    ) {
        content()
    }
}

@Composable
fun AnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        content()
    }
}

@Composable
fun MediaBackButton(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.5f)
) {
    IconButton(
        onClick = onBackClick,
        modifier = modifier.background(
            backgroundColor,
            CircleShape
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back),
            tint = Color.White
        )
    }
}

@Composable
fun MediaCounter(
    currentIndex: Int,
    totalCount: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black.copy(alpha = 0.5f)
) {
    Text(
        text = "${currentIndex + 1} / $totalCount",
        color = Color.White,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
            .background(
                backgroundColor,
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = Color.White
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color
    )
}

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(
        Color.Transparent,
        Color.Black.copy(alpha = 0.7f)
    ),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(colors = colors)
        )
    ) {
        content()
    }
}

@Composable
fun AudioFileIcon(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    tint: Color = Color.White
) {
    Icon(
        painter = painterResource(R.drawable.audio_file),
        contentDescription = stringResource(R.string.audio_file),
        tint = tint,
        modifier = modifier.size(size)
    )
}

@Composable
fun ClickableArea(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    indication: Indication? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.clickable(
            indication = indication,
            interactionSource = remember { MutableInteractionSource() }
        ) { onClick() }
    ) {
        content()
    }
}
