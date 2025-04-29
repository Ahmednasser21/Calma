package com.metafortech.calma.presentation.welcom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.metafortech.calma.R
import com.metafortech.calma.theme.Gray

@Composable
fun LanguageScreen(
    modifier: Modifier = Modifier,
    onLanguageClick: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LanguageItem(
                language = stringResource(R.string.arabic_language),
                buttonText = stringResource(R.string.a_start),
                buttonColor = Gray,
                onClick = { onLanguageClick("ar") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LanguageItem(
                language = stringResource(R.string.english_language),
                buttonText = stringResource(R.string.e_start),
                buttonColor = MaterialTheme.colorScheme.primary,
                onClick = { onLanguageClick("en") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LanguageItem(
                language = stringResource(R.string.french_language),
                buttonText = stringResource(R.string.f_start),
                buttonColor = MaterialTheme.colorScheme.secondary,
                onClick = { onLanguageClick("fr") }
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.sports_illustration),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.width(230.dp).height(300.dp)
            )
        }
    }
}

@Composable
fun LanguageItem(
    language: String,
    buttonText: String,
    buttonColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = language,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )

        Button(
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            modifier = Modifier
                .width(270.dp)
                .height(90.dp)
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}