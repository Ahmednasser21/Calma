package com.metafortech.calma.presentation.authentication.verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.metafortech.calma.R
import com.metafortech.calma.presentation.ErrorStateIndicator
import com.metafortech.calma.presentation.LoadingStateIndicator
import com.metafortech.calma.presentation.NextButton

@Composable
fun PhoneVerificationScreen(
    modifier: Modifier = Modifier,
    state: PhoneVerificationUiState,
    onCodeValueChange: (Int, String) -> Unit,
    onResendCodeClick: () -> Unit,
    onNextClick: () -> Unit,
    phoneNumber: String
) {

    LoadingStateIndicator(state.isLoading) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.verify_phone),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.verification_code_sent))
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(" $phoneNumber ")
                    }
                },
                modifier = Modifier.padding(bottom = 24.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(R.string.verification_code_sent_2),
                modifier = Modifier.padding(bottom = 24.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            OtpCodeFields(
                otpCount = 4,
                otpValues = state.codeValues,
                onValueChange = onCodeValueChange
            )

            CountdownTimer(
                modifier = Modifier.padding(top = 16.dp),
                remainingTime = state.remainingTime
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.didnt_receive_code),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                TextButton(
                    onClick = onResendCodeClick,
                    enabled = state.remainingTime == 0,
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.secondary,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Gray
                    )
                ) {
                    Text(
                        text = stringResource(R.string.resend_code),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            ErrorStateIndicator(
                error = state.errorMessage,
                onRetry = null
            )

            NextButton(
                modifier = Modifier.padding(top = 16.dp),
                enabled = state.isCodeComplete
            ) {
                onNextClick()
            }

        }
    }
}

@Composable
fun OtpCodeFields(
    otpCount: Int,
    otpValues: List<String>,
    onValueChange: (Int, String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(otpCount) { index ->
            OtpCodeField(
                value = otpValues.getOrElse(index) { "" },
                onValueChange = { value -> onValueChange(index, value) }
            )
        }
    }
}

@Composable
fun OtpCodeField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 1) onValueChange(it)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        textStyle = MaterialTheme.typography.titleLarge.copy(
            textAlign = TextAlign.Center
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp),
        placeholder = {
            Text(
                text = "-",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()

            )
        }
    )
}

@Composable
fun CountdownTimer(
    modifier: Modifier = Modifier,
    remainingTime: Int
) {
    val minutes = remainingTime / 60
    val seconds = remainingTime % 60
    val formattedTime = "%d:%02d".format(minutes, seconds)

    Text(
        modifier = modifier,
        text = formattedTime,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary
    )
}
