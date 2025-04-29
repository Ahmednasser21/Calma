package com.metafortech.calma.presentation.authentication.login.presentation

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.metafortech.calma.R
import com.metafortech.calma.presentation.authentication.BottomPartOfLoginAndRegisterScreen
import com.metafortech.calma.presentation.authentication.GeneralTextField
import com.metafortech.calma.presentation.authentication.PasswordTextField
import com.metafortech.calma.presentation.authentication.TextButton


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginScreenUIState,
    onDismiss: () -> Unit = {},
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    onLoginClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginWithGoogleClick: (Context) -> Unit,
    onLoginWithFacebookClick: () -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(true) }
    if (uiState.loginSuccess) {
        showDialog = false
        onLoginSuccess()
    }
    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
                onDismiss()
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.BottomCenter
            ) {

                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
                        )
                        .padding(horizontal = 16.dp)
                ) {
                    LoginScreenContents(
                        modifier = Modifier,
                        email = uiState.email,
                        password = uiState.password,
                        errorMessages = uiState.errorMessageResId,
                        loginError = uiState.loginError,
                        isLoading = uiState.isLoading,
                        onEmailValueChange = { email ->
                            onEmailValueChange(
                                email
                            )
                        },
                        onPasswordValueChange = { password ->
                            onPasswordValueChange(password)
                        },
                        onForgotPasswordClick = {
                            showDialog = false
                            onForgotPasswordClick()
                        },
                        onLoginClick = { onLoginClick() },
                        onRegisterClick = {
                            showDialog = false
                            onRegisterClick()
                        },
                        onLoginWithGoogleClick = {
                            onLoginWithGoogleClick(context)
                        },
                        onLoginWithFacebookClick = { onLoginWithFacebookClick() }
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreenContents(
    modifier: Modifier = Modifier,
    email: String,
    password: String = "",
    errorMessages: Int?,
    loginError: String?,
    isLoading: Boolean,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginWithGoogleClick: () -> Unit,
    onLoginWithFacebookClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.welcome_back))
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    append(" ${stringResource(R.string.calma)}")
                }
            },
            modifier = modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.login_with_email),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        GeneralTextField(
            modifier = Modifier,
            textValue = email,
            label = stringResource(R.string.email_label),
            placeHolder = stringResource(R.string.email_placeholder),
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ) { email ->
            onEmailValueChange(email)
        }

        PasswordTextField(
            password = password
        ) { password ->
            onPasswordValueChange(password)
        }
        errorMessages?.let { errorMessage ->
            Text(
                text = stringResource(errorMessage),
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
        loginError?.let { errorMessage ->
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 4.dp),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Red
            )
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp),
                color = MaterialTheme.colorScheme.secondary,
                strokeWidth = 2.dp
            )
        }
        TextButton(
            modifier = Modifier
                .align(Alignment.Start),
            stringResource(R.string.forgot_password)
        ) {
            onForgotPasswordClick()
        }
        BottomPartOfLoginAndRegisterScreen(
            modifier = modifier,
            buttonText = stringResource(R.string.login),
            text = stringResource(R.string.dont_have_acc),
            textButtonText = stringResource(R.string.create_acc_l),
            onButtonClick = { onLoginClick() },
            onTextButtonClick = { onRegisterClick() },
            onLoginWithGoogleClick = { onLoginWithGoogleClick() },
            onLoginWithFacebookClick = { onLoginWithFacebookClick }

        )
    }
}

