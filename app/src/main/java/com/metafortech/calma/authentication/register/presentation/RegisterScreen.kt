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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.metafortech.calma.authentication.BottomPartOfLoginAndRegisterScreen
import com.metafortech.calma.authentication.GeneralTextField
import com.metafortech.calma.authentication.PasswordTextField
import com.metafortech.calma.authentication.register.presentation.CountryData.countries
import java.util.Calendar
import java.util.Locale

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    state: RegisterUiState = RegisterUiState(),
    onNameValueChange: (String) -> Unit = {},
    onEmailValueChange: (String) -> Unit = {},
    onPhoneNumberChange: (String) -> Unit = {},
    onCountryClick: (Country) -> Unit = {},
    onSheetOpenChange: (Boolean) -> Unit = {},
    onSearchQueryChange: (String) -> Unit = {},
    onPasswordValueChange: (String) -> Unit = {},
    onShowDatePickerChange: (Boolean) -> Unit = {},
    onBirthdayValueChange: (String) -> Unit = {},
    onGenderClick: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onLoginWithGoogleClick: () -> Unit = {},
    onLoginWithFacebookClick: () -> Unit = {}
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
            selectedCountry = state.country,
            searchQuery = state.searchQuery,
            onSearchQueryChange = { searchQuery ->
                onSearchQueryChange(searchQuery)
            },
            isSheetOpen = state.isSheetOpen,
            onSheetOpenChange = { isSheetOpen ->
                onSheetOpenChange(isSheetOpen)
            },
            onCountryClick = { country ->
                onCountryClick(country)
            }

        )
        PasswordTextField(
            password = state.password
        ) { password ->
            onPasswordValueChange(password)
        }
        BirthdayField(
            birthday = state.birthday,
            showDatePicker = state.showDatePicker,
            onShowDatePickerChange = { showDatePicker ->
                onShowDatePickerChange(showDatePicker)
            },
            onBirthdayValueChange = { birthday ->
                onBirthdayValueChange(birthday)
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, top = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.gender),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            GenderSelection(state.gender) { selectedGender ->
                onGenderClick(selectedGender)
            }
        }
        BottomPartOfLoginAndRegisterScreen(
            onButtonClick = {
                onRegisterClick()
            },
            buttonText = stringResource(R.string.create_acc),
            text = stringResource(R.string.already_have_account),
            textButtonText = stringResource(R.string.login),
            onTextButtonClick = {
                onLoginClick()
            },
            onLoginWithGoogleClick = {
                onLoginWithGoogleClick()
            },
            onLoginWithFacebookClick = {
                onLoginWithFacebookClick()
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 32.dp)
            ) {
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
    selectedCountry: Country,
    searchQuery: String,
    isSheetOpen: Boolean,
    onSheetOpenChange: (Boolean) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onCountryClick: (Country) -> Unit
) {

    Box(modifier = modifier.fillMaxWidth()) {
        PhoneNumberField(
            selectedCountry = selectedCountry,
            phoneNumber = phoneNumber,
            onPhoneNumberChange = { phoneNumber ->
                onPhoneNumberChange(phoneNumber)
            },
            onCountryClick = { onSheetOpenChange(isSheetOpen) }
        )

        if (isSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { onSheetOpenChange(isSheetOpen) },
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
                            onValueChange = { searchQuery ->
                                onSearchQueryChange(searchQuery)
                            },
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
                                            onCountryClick(country)
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
                                onClick = { onSheetOpenChange(isSheetOpen) },
                                modifier = Modifier
                                    .width(240.dp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayField(
    modifier: Modifier = Modifier,
    birthday: String,
    showDatePicker: Boolean,
    onShowDatePickerChange: (Boolean) -> Unit = {},
    onBirthdayValueChange: (String) -> Unit
) {

    val maxDate = Calendar.getInstance()
    maxDate.add(Calendar.YEAR, -10)

    val minDate = Calendar.getInstance()
    minDate.add(Calendar.YEAR, -100)

    val initialDateMillis = maxDate.timeInMillis

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis in minDate.timeInMillis..maxDate.timeInMillis
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                onShowDatePickerChange(showDatePicker)
            },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { dateMillis ->
                            val cal = Calendar.getInstance()
                            cal.timeInMillis = dateMillis
                            val year = cal.get(Calendar.YEAR)
                            val month = cal.get(Calendar.MONTH) + 1
                            val day = cal.get(Calendar.DAY_OF_MONTH)
                            val selectedDate =
                                String.format(Locale.ROOT, "%d-%02d-%02d", year, month, day)
                            onBirthdayValueChange(selectedDate)
                        }
                        onShowDatePickerChange(showDatePicker)
                    },
                    modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = stringResource(R.string.ok),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onShowDatePickerChange(showDatePicker)
                    },
                    modifier = modifier.padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.secondary,
                headlineContentColor = MaterialTheme.colorScheme.secondary,
                selectedDayContainerColor = MaterialTheme.colorScheme.secondary,
                todayDateBorderColor = MaterialTheme.colorScheme.secondary,
                todayContentColor = MaterialTheme.colorScheme.secondary,
                currentYearContentColor = MaterialTheme.colorScheme.secondary,
                weekdayContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                dayContentColor = MaterialTheme.colorScheme.onSurface,
                yearContentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                    headlineContentColor = MaterialTheme.colorScheme.secondary,
                    selectedDayContainerColor = MaterialTheme.colorScheme.secondary,
                    todayDateBorderColor = MaterialTheme.colorScheme.secondary,
                    todayContentColor = MaterialTheme.colorScheme.secondary,
                    currentYearContentColor = MaterialTheme.colorScheme.secondary,
                    weekdayContentColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    yearContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }

    GeneralTextField(
        modifier = modifier
            .padding(top = 12.dp)
            .clickable {
                onShowDatePickerChange(showDatePicker)
            },
        textValue = birthday,
        label = stringResource(R.string.birth_day),
        placeHolder = stringResource(R.string.date_placeholder),
        imeAction = ImeAction.Done,
        keyboardType = KeyboardType.Unspecified,
        enabled = false
    ) { newBirthday ->
        onBirthdayValueChange(newBirthday)
    }
}


@Composable
fun GenderRadioButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: (String) -> Unit,
    selected: Boolean = false
) {
    val backgroundColor = if (selected) Color(0xFF3B3F3C) else Color(0xFFE1DCDC)
    val textColor = if (selected) Color.White else Color.Black

    Button(
        onClick = {
            onClick(text)
        },
        modifier = modifier
            .width(100.dp)
            .height(40.dp),
        shape = RoundedCornerShape(25.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

@Composable
fun GenderSelection(selectedGender: String, onGenderClick: (String) -> Unit) {

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        GenderRadioButton(
            text = stringResource(R.string.male),
            onClick = { _ ->
                onGenderClick("1")
            },
            selected = selectedGender == "1"
        )
        GenderRadioButton(
            text = stringResource(R.string.female),
            onClick = { _ ->
                onGenderClick("2")
            },
            selected = selectedGender == "2"
        )
    }
}