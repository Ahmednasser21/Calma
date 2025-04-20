package com.metafortech.calma.login.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.metafortech.calma.theme.Black
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.metafortech.calma.R


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    email: String = "",
    isEmailError: Boolean = false,
    password: String = "",
    isPasswordError: Boolean = false,
    onEmailValueChange: (String) -> Unit = {},
    onPasswordValueChange: (String) -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
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
        Column(
            modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .padding(16.dp)
            ) {
                LoginScreenContents(
                    modifier = modifier,
                    email = email,
                    isEmailError = isEmailError,
                    password = password,
                    isPasswordError = isPasswordError,
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
                    onLoginWithFacebookClick = { onLoginWithFacebookClick() },

                    )
            }
        }
    }
}

@Composable
fun LoginScreenContents(
    modifier: Modifier = Modifier,
    email: String,
    isEmailError: Boolean,
    password: String,
    isPasswordError: Boolean,
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
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.titleLarge,
            color = Black
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
            email = email, isEmailError = isEmailError
        ) { email ->
            onEmailValueChange(email)
        }
        PasswordTextField(
            password = password, isPasswordError = isPasswordError
        ) { password ->
            onPasswordValueChange(password)
        }
        TextButton(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 16.dp),
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

@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    email: String,
    isEmailError: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 16.dp),
        value = email,
        onValueChange = { value ->
            onValueChange(value)
        },
        placeholder = {
            Text(
                text = stringResource(R.string.email_placeholder),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        label = {
            Text(
                text = stringResource(
                    R.string.email_label
                ), style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(50.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
        ),
        isError = isEmailError,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.LightGray,
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            errorBorderColor = Color.Red,
            cursorColor = MaterialTheme.colorScheme.secondary,
        )
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    password: String,
    isPasswordError: Boolean,
    onValueChange: (String) -> Unit
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
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
        isError = isPasswordError,
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
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
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
            .width(180.dp)
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
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onLoginWithGoogleClick: () -> Unit,
    onLoginWithFacebookClick: () -> Unit
) {
    LoginButton(stringResource(R.string.login)) {
        onLoginClick()
    }
    Row(
        modifier = modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.dont_have_acc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        TextButton(text = stringResource(R.string.create_acc)) {
            onRegisterClick()
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
            .padding(16.dp),
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