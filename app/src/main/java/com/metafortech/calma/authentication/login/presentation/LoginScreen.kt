package com.metafortech.calma.authentication.login.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.metafortech.calma.theme.Black
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.metafortech.calma.R
import com.metafortech.calma.authentication.BottomPartOfLoginAndRegisterScreen
import com.metafortech.calma.authentication.EmailTextField
import com.metafortech.calma.authentication.PasswordTextField
import com.metafortech.calma.authentication.TextButton


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    uiState: LoginScreenUIState,
    onEmailValueChange: (String) -> Unit = {},
    onPasswordValueChange: (String) -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onLoginWithGoogleClick: () -> Unit = {},
    onLoginWithFacebookClick: () -> Unit = {}
) {

    Dialog(
        onDismissRequest = onDismiss, properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        if (uiState.loginSuccess) onLoginSuccess()

        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(horizontal = 16.dp)
            ) {
                LoginScreenContents(
                    modifier = Modifier,
                    email = uiState.email,
                    password = uiState.password,
                    errorMessages = uiState.errorMessageResId,
                    isLoading = uiState.isLoading,
                    onEmailValueChange = { email ->
                        onEmailValueChange(
                            email
                        )
                    },
                    onPasswordValueChange = { password ->
                        onPasswordValueChange(password)
                    },
                    onForgotPasswordClick = { onForgotPasswordClick() },
                    onLoginClick = { onLoginClick() },
                    onRegisterClick = { onRegisterClick() },
                    onLoginWithGoogleClick = { onLoginWithGoogleClick() },
                    onLoginWithFacebookClick = { onLoginWithFacebookClick() })
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
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.welcome_back),
                style = MaterialTheme.typography.bodyMedium,
                color = Black
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = stringResource(R.string.calma),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Text(
            text = stringResource(R.string.login_with_email),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        EmailTextField(
            modifier = Modifier,
            email = email,
            placeHolder = stringResource(R.string.email_placeholder),
            imeAction = ImeAction.Next
        ) { email ->
            onEmailValueChange(email)
        }
        Spacer(modifier = Modifier.height(16.dp))

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
                .align(Alignment.Start)
                .padding(bottom = 4.dp),
            stringResource(R.string.forgot_password)
        ) {
            onForgotPasswordClick()
        }
        BottomPartOfLoginAndRegisterScreen(
            modifier = modifier,
            onLoginClick = { onLoginClick() },
            onRegisterClick = { onRegisterClick() },
            onLoginWithGoogleClick = { onLoginWithGoogleClick() },
            onLoginWithFacebookClick = { onLoginWithFacebookClick }

        )
    }
}

