package com.metafortech.calma.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.metafortech.calma.R

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
                contentDescription = "back"
            )
        }
    }
}

@Composable
fun ImageLoading(
    imageURL: String,
    contentDescription: String?,
    modifier: Modifier
) {
    val painter = rememberAsyncImagePainter(imageURL)
    val state by painter.state.collectAsState()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (state is AsyncImagePainter.State.Loading) {
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
        }
    }
}
@Composable
fun LoadingStateIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
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