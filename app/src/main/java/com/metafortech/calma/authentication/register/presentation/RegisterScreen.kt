package com.metafortech.calma.authentication.register.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.svg.SvgDecoder
import com.metafortech.calma.R
import com.metafortech.calma.authentication.GeneralTextField
import com.metafortech.calma.authentication.register.presentation.CountryData.countries

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    state: RegisterUiState = RegisterUiState(),
    onNameValueChange: (String) -> Unit = {},
    onEmailValueChange: (String) -> Unit = {},
    onPhoneNumberChange: (String) -> Unit = {},
    onCountryClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.create_acc_r),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.enter_your_data))
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    append(" ${stringResource(R.string.calma)}")
                }
            },
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        GeneralTextField(
            textValue = state.name,
            placeHolder = stringResource(R.string.full_name),
            label = stringResource(R.string.user_name),
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text,
        ) { value ->
            onNameValueChange(value)
        }
        GeneralTextField(
            textValue = state.email,
            label = stringResource(R.string.email_label),
            placeHolder = stringResource(R.string.email_placeholder),
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ) { email ->
            onEmailValueChange(email)
        }
        PhoneNumberWithCountryPicker(
            modifier = Modifier.padding(bottom = 16.dp),
            phoneNumber = state.phoneNumber,
            onPhoneNumberChange = { phoneNumber ->
                onPhoneNumberChange(phoneNumber)
            },
            onCountryClick = { dialCode ->
                onCountryClick(dialCode)
            }

        )
    }

}

@Composable
fun PhoneNumberField(
    selectedCountry: Country,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onCountryClick: () -> Unit
) {
    OutlinedTextField(
        value = phoneNumber,
        onValueChange = { value ->
            onPhoneNumberChange(value)
        },
        label = {
            Text(
                text = stringResource(R.string.phone_num),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(50.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        leadingIcon = {
            PhoneNumberFieldLeadingIcon(selectedCountry, onCountryClick)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone
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
fun PhoneNumberFieldLeadingIcon(selectedCountry: Country, onCountryClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onCountryClick() }
            .padding(start = 8.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(selectedCountry.flagUrl)
                .build(),
            imageLoader = ImageLoader.Builder(LocalContext.current)
                .components {
                    add(SvgDecoder.Factory())
                }
                .build(),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = selectedCountry.code,
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        VerticalDivider(
            modifier = Modifier
                .width(1.dp)
                .height(24.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun CountryItem(
    country: Country,
    onClick: () -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            AsyncImage(
                model = country.flagUrl,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier
                .weight(1f)
                .padding(end = 32.dp)) {
                Text(
                    text = country.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = country.nameEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = "(${country.dialCode})",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberWithCountryPicker(
    modifier: Modifier = Modifier,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onCountryClick: (String) -> Unit
) {
    var selectedCountry by remember { mutableStateOf(countries[41]) }
    var isSheetOpen by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxWidth()) {
        PhoneNumberField(
            selectedCountry = selectedCountry,
            phoneNumber = phoneNumber,
            onPhoneNumberChange = { phoneNumber ->
                onPhoneNumberChange(phoneNumber)
            },
            onCountryClick = { isSheetOpen = true }
        )

        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { isSheetOpen = false },
                sheetState = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true,
                    confirmValueChange = { true }
                ),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.onBackground,
                tonalElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = stringResource(R.string.select_country),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 4.dp),
                            textAlign = TextAlign.Center
                        )

                        // Search Field
                        OutlinedTextField(
                            value = searchQuery,
                            textStyle = MaterialTheme.typography.bodyMedium,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .padding(bottom = 4.dp, start = 32.dp, end = 32.dp)
                                .background(
                                    color = Color(0x42C8C8C8),
                                    shape = RoundedCornerShape(50.dp)
                                ),
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.search),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(50.dp),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "search country"
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                            )
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(270.dp)
                        ) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(
                                    items = countries.filter { country ->
                                        country.name.startsWith(searchQuery, ignoreCase = true) ||
                                                country.nameEn.startsWith(
                                                    searchQuery,
                                                    ignoreCase = true
                                                )
                                    },
                                    key = { it.code }
                                ) { country ->
                                    CountryItem(
                                        country = country,
                                        onClick = {
                                            selectedCountry = country
//                                            isSheetOpen = false
                                            onCountryClick(country.dialCode)
                                        }
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = MaterialTheme.colorScheme.primary,
                                        thickness = 1.dp
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = { isSheetOpen = false },
                                modifier = Modifier.width(240.dp)
                                    .height(56.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Text(
                                    text = stringResource(R.string.next),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}