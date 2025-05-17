package com.metafortech.calma.presentation.authentication

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.metafortech.calma.LanguageScreen
import com.metafortech.calma.data.remote.register.RegisterBody
import com.metafortech.calma.presentation.authentication.NavigationEvent.VerificationScreen
import com.metafortech.calma.presentation.authentication.interest.InterestSelectionScreen
import com.metafortech.calma.presentation.authentication.interest.InterestSelectionViewModel
import com.metafortech.calma.presentation.authentication.login.LoginScreen
import com.metafortech.calma.presentation.authentication.login.LoginViewModel
import com.metafortech.calma.presentation.authentication.register.RegisterScreen
import com.metafortech.calma.presentation.authentication.register.RegisterViewModel
import com.metafortech.calma.presentation.authentication.sport.SportSelectionScreen
import com.metafortech.calma.presentation.authentication.sport.SportSelectionViewModel
import com.metafortech.calma.presentation.authentication.verification.PhoneVerificationScreen
import com.metafortech.calma.presentation.authentication.verification.PhoneVerificationViewModel
import com.metafortech.calma.presentation.home.HomeNav
import com.metafortech.calma.presentation.welcom.LanguageScreenViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

fun NavGraphBuilder.authNav(
    innerPadding: PaddingValues,
    navController: NavHostController,
) {
    navigation<AuthNav>(startDestination = LoginScreen) {

        composable<LoginScreen> {
            val loginViewModel: LoginViewModel = hiltViewModel()
            val state = loginViewModel.uiState.collectAsStateWithLifecycle().value
            LoginScreen(
                modifier = Modifier.padding(innerPadding),
                uiState = state,
                onEmailValueChange = { email ->
                    loginViewModel.onEmailChange(email)
                },
                onPasswordValueChange = { password ->
                    loginViewModel.onPasswordChange(password)
                },
                onLoginClick = { loginViewModel.onLoginClick() },
                onLoginSuccess = {
                    navController.navigate(HomeNav) {
                        popUpTo(LanguageScreen) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onRegisterClick = { navController.navigate(RegisterScreen) },
                onDismiss = { navController.navigate(LanguageScreen) },
                onLoginWithGoogleClick = { loginViewModel.onLoginWithGoogleClick(it) },
                onLoginWithFacebookClick = { loginViewModel.onLoginWithFacebookClick() }
            )
        }
        composable<RegisterScreen> {
            val registerViewModel: RegisterViewModel = hiltViewModel()
            val languageScreenViewModel: LanguageScreenViewModel = hiltViewModel()
            val registerState = registerViewModel.uiState.collectAsStateWithLifecycle().value
            RegisterScreen(
                modifier = Modifier.padding(innerPadding),
                state = registerState,
                onNameValueChange = { name ->
                    registerViewModel.onNameValueChange(name)
                },
                onEmailValueChange = { email ->
                    registerViewModel.onEmailValueChange(email)
                },
                onPhoneNumberChange = { phoneNumber ->
                    registerViewModel.onPhoneNumberChange(phoneNumber)
                },
                onCountryClick = { country ->
                    registerViewModel.onCountryClick(country)
                },
                onSheetOpenChange = { isSheetOpen ->
                    registerViewModel.onSheetOpenChange(isSheetOpen)
                },
                onSearchQueryChange = { searchQuery ->
                    registerViewModel.onSearchQueryChange(searchQuery)
                },
                onPasswordValueChange = { password ->
                    registerViewModel.onPasswordValueChange(password)
                },
                onShowDatePickerChange = { showDatePicker ->
                    registerViewModel.onShowDatePickerChange(showDatePicker)
                },
                onBirthdayValueChange = { birthday ->
                    registerViewModel.onBirthdayValueChange(birthday)
                },
                onGenderClick = { gender ->
                    registerViewModel.onGenderClick(gender)
                },
                onRegisterClick = {
                    val registerDTO = RegisterDTO(
                        name = registerState.name,
                        email = registerState.email,
                        dop = registerState.birthday,
                        password = registerState.password,
                        phone = registerState.country.dialCode + registerState.phoneNumber,
                        gender = registerState.gender
                    )
                    val registerJson =
                        Json.encodeToString(registerDTO.copy(lang = languageScreenViewModel.currentLanguage.value.toString()))
                    registerViewModel.onRegisterClick {
                        navController.navigate(
                            InterestSelectionScreen(
                                registerJsonInterest = registerJson
                            )
                        )
                    }
                },
                onLoginClick = { navController.navigate(LoginScreen) },
                onLoginWithGoogleClick = { registerViewModel.onRegisterWithGoogleClick(it) },
                onLoginWithFacebookClick = {
                    registerViewModel.onLoginWithFacebookClick()
                }

            )
        }
        composable<InterestSelectionScreen> { backStackEntry ->
            val interestSelectionViewModel: InterestSelectionViewModel = hiltViewModel()
            val screenArg = backStackEntry.toRoute<InterestSelectionScreen>()
            val registerDTO = Json.decodeFromString<RegisterDTO>(screenArg.registerJsonInterest)
            val state = interestSelectionViewModel.interestUIState.collectAsStateWithLifecycle().value
            InterestSelectionScreen(
                modifier = Modifier.padding(innerPadding),
                state = state ,
                onBackClick = { navController.popBackStack() },
                onInterestSelected = { selectedInterest ->
                    interestSelectionViewModel.onInterestSelected(selectedInterest)
                },
                selectedLang = registerDTO.lang.toString(),
                onNextClick = {
                    val registerJson =
                        Json.encodeToString(
                            registerDTO.copy(
                                interests = listOf(state.selectedInterestId?:0)
                            )
                        )
                    navController.navigate(SportSelectionScreen(registerJson))
                }
            )

        }
        composable<SportSelectionScreen> { backStackEntry ->
            val sportSelectionViewModel: SportSelectionViewModel = hiltViewModel()
            val state = sportSelectionViewModel.uiState.collectAsStateWithLifecycle().value
            val screenArg = backStackEntry.toRoute<SportSelectionScreen>()
            val registerDTO = Json.decodeFromString<RegisterDTO>(screenArg.registerJsonSport)
            SportSelectionScreen(
                state = state,
                modifier = Modifier.padding(innerPadding),
                onBackClick = { navController.popBackStack() },
                selectSport = { id ->
                    sportSelectionViewModel.selectSport(id)
                },
                onNextClick = {
                    val updatedDTO = registerDTO.copy(sports = listOf(state.selectedSportId?:0))
                    sportSelectionViewModel.register(
                        RegisterBody(
                            name = updatedDTO.name,
                            email = updatedDTO.email,
                            dob = updatedDTO.dop,
                            password = updatedDTO.password,
                            phone = updatedDTO.phone,
                            gendar = updatedDTO.gender,
                            lang = updatedDTO.lang.toString(),
                            sports = updatedDTO.sports ?: emptyList(),
                            intersets = updatedDTO.interests ?: emptyList()
                        )
                    )

                },
                onRegisterSuccess = {
                    LaunchedEffect(Unit) {
                        sportSelectionViewModel.navigationEvent.collect { event ->
                            when (event) {
                                is VerificationScreen ->
                                    navController.navigate(VerificationScreen(event.phoneNumber))
                            }
                        }
                    }

                },
                selectedLang = registerDTO.lang.toString()
            )
        }
        composable<VerificationScreen> {
            val phoneVerificationViewModel: PhoneVerificationViewModel = hiltViewModel()
            val state = phoneVerificationViewModel.uiState.collectAsStateWithLifecycle().value
            val screenArgs = it.toRoute<VerificationScreen>()
            PhoneVerificationScreen(
                modifier = Modifier.padding(innerPadding),
                state = state,
                onCodeValueChange = phoneVerificationViewModel::onCodeValueChange,
                onResendCodeClick = phoneVerificationViewModel::onResendCode,
                onNextClick = {
                    phoneVerificationViewModel.onNextClick(
                        navigate = {
                            navController.navigate(LoginScreen)
                        }
                    )
                },
                phoneNumber = screenArgs.phoneNumber
            )

        }
    }
}

@Serializable
object AuthNav

@Serializable
object LoginScreen

@Serializable
object RegisterScreen

@Serializable
data class InterestSelectionScreen(val registerJsonInterest: String)

@Serializable
data class SportSelectionScreen(val registerJsonSport: String)

sealed class NavigationEvent() {
    @Serializable
    data class VerificationScreen(val phoneNumber: String): NavigationEvent()
}