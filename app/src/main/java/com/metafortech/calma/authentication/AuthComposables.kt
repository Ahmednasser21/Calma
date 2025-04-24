package com.metafortech.calma.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    placeHolder: String,
    label: String,
    imeAction: ImeAction,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = textValue,
        onValueChange = { value ->
            onValueChange(value)
        },
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
            .width(200.dp)
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
        TextButton(text = stringResource(R.string.create_acc_l)) {
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
            .padding(32.dp),
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
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center
            )
        }
    }
}