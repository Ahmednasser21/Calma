package com.metafortech.calma.presentation.authentication

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.metafortech.calma.WelcomeScreen
import com.metafortech.calma.presentation.authentication.interest.InterestSelectionScreen
import com.metafortech.calma.presentation.authentication.interest.InterestSelectionViewModel
import com.metafortech.calma.presentation.authentication.login.presentation.LoginScreen
import com.metafortech.calma.presentation.authentication.login.presentation.LoginViewModel
import com.metafortech.calma.presentation.authentication.register.presentation.RegisterScreen
import com.metafortech.calma.presentation.authentication.register.presentation.RegisterViewModel
import com.metafortech.calma.presentation.authentication.sport.SportSelectionScreen
import com.metafortech.calma.presentation.authentication.sport.SportSelectionViewModel
import com.metafortech.calma.presentation.authentication.verification.PhoneVerificationScreen
import com.metafortech.calma.presentation.authentication.verification.PhoneVerificationViewModel
import kotlinx.serialization.Serializable

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
                onLoginSuccess = {},
                onRegisterClick = { navController.navigate(RegisterScreen) },
                onDismiss = { navController.navigate(WelcomeScreen) },
                onLoginWithGoogleClick = { loginViewModel.onLoginWithGoogleClick(it) },
                onLoginWithFacebookClick = { loginViewModel.onLoginWithFacebookClick() }
            )
        }
        composable<RegisterScreen> {
            val registerViewModel: RegisterViewModel = hiltViewModel()
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
                onRegisterClick = { registerViewModel.onRegisterClick() },
                onLoginClick = { navController.navigate(LoginScreen) },
                onLoginWithGoogleClick = { registerViewModel.onRegisterWithGoogleClick(it) },
                onLoginWithFacebookClick = {
                    registerViewModel.onLoginWithFacebookClick()
                    navController.navigate(VerificationScreen(phoneNumber = (registerState.country.dialCode + registerState.phoneNumber)))

                }

            )
        }
        composable<VerificationScreen> {
            val phoneVerificationViewModel: PhoneVerificationViewModel = hiltViewModel()
            val state = phoneVerificationViewModel.uiState.collectAsStateWithLifecycle().value
            PhoneVerificationScreen(
                modifier = Modifier.padding(innerPadding),
                state = state,
                onCodeValueChange = phoneVerificationViewModel::onCodeValueChange,
                onResendCodeClick = phoneVerificationViewModel::onResendCode,
                onNextClick = {
                    phoneVerificationViewModel.onNextClick(
                        navigate = { navController.navigate(InterestSelectionScreen) }
                    )
                },
                phoneNumber = it.arguments?.getString("phoneNumber") ?: "",
            )

        }
        composable<InterestSelectionScreen> {
            val interestSelectionViewModel: InterestSelectionViewModel = hiltViewModel()
            val selectedInterest = interestSelectionViewModel.selectedInterest.value
            InterestSelectionScreen(
                modifier = Modifier.padding(innerPadding),
                onBackClick = { navController.popBackStack() },
                onInterestSelected = interestSelectionViewModel::onInterestSelected,
                onNextClick = {
                    navController.navigate(SportSelectionScreen(selectedInterest.toString()))
                },
                selectedInterest = selectedInterest
            )

        }
        composable<SportSelectionScreen> {
            val sportSelectionViewModel: SportSelectionViewModel = hiltViewModel()
            SportSelectionScreen(
                state = sportSelectionViewModel.uiState.collectAsStateWithLifecycle().value,
                modifier = Modifier.padding(innerPadding),
                onBackClick = { navController.popBackStack() },
                selectSport = sportSelectionViewModel::selectSport,
                onNextClick = {
                    sportSelectionViewModel.onNextClick {
                        navController.navigate(WelcomeScreen)
                        navController.popBackStack(AuthNav, true)
                    }
                }
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
data class VerificationScreen(val phoneNumber: String)

@Serializable
object InterestSelectionScreen

@Serializable
data class SportSelectionScreen(val interest: String)