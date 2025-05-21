package com.metafortech.calma.presentation.authentication.login

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.metafortech.calma.R
import com.metafortech.calma.presentation.BottomPartOfLoginAndRegisterScreen
import com.metafortech.calma.presentation.ErrorStateIndicator
import com.metafortech.calma.presentation.GeneralTextField
import com.metafortech.calma.presentation.LoadingStateIndicator
import com.metafortech.calma.presentation.PasswordTextField
import com.metafortech.calma.presentation.TextButton


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginScreenUIState,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onForgotPasswordClick: () -> Unit = {},
    onLoginClick: () -> Unit,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginWithGoogleClick: (Context) -> Unit,
    onLoginWithFacebookClick: () -> Unit
) {
    LoadingStateIndicator(uiState.isLoading) {
        val context = LocalContext.current
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xBE272727)),
            contentAlignment = Alignment.BottomCenter
        ) {

            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.78f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topStart = 48.dp, topEnd = 48.dp)
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                LoginScreenContents(
                    modifier = Modifier,
                    email = uiState.email,
                    password = uiState.password,
                    errorMessage = uiState.errorMessageResId?.let { stringResource(it) }
                        ?: uiState.loginError,
                    onEmailValueChange = { email ->
                        onEmailValueChange(
                            email
                        )
                    },
                    onPasswordValueChange = { password ->
                        onPasswordValueChange(password)
                    },
                    onForgotPasswordClick = {
                        onForgotPasswordClick()
                    },
                    onLoginClick = { onLoginClick() },
                    onRegisterClick = {
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

@Composable
fun LoginScreenContents(
    modifier: Modifier = Modifier,
    email: String,
    password: String = "",
    errorMessage: String?,
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
            label = stringResource(R.string.email_phone_label),
            placeHolder = stringResource(R.string.email_phone_placeholder),
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

        ErrorStateIndicator(
            error = errorMessage,
            onRetry = null
        )

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

