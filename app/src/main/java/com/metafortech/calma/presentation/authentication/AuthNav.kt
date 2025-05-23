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
import com.metafortech.calma.data.remote.interest.InterestsUpdateRequest
import com.metafortech.calma.presentation.AppRoute.AuthNav
import com.metafortech.calma.presentation.AppRoute.HomeNav
import com.metafortech.calma.presentation.AppRoute.InterestSelectionScreen
import com.metafortech.calma.presentation.AppRoute.LoginScreen
import com.metafortech.calma.presentation.AppRoute.RegisterScreen
import com.metafortech.calma.presentation.AppRoute.SportSelectionScreen
import com.metafortech.calma.presentation.AppRoute.VerificationScreen
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
                    navController.navigate(HomeNav(
                        userName = state.userName,
                        userImageUrl = state.userImageUrl
                    )) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(RegisterScreen) },
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
                onRegisterClick = {
                    registerViewModel.onRegisterClick()
                },
                onLoginClick = { navController.navigate(LoginScreen) },
                onLoginWithGoogleClick = { registerViewModel.onRegisterWithGoogleClick(it) },
                onLoginWithFacebookClick = {
                    registerViewModel.onLoginWithFacebookClick()
                },
                onRegisterSuccess = {
                    LaunchedEffect(Unit) {
                        registerViewModel.navigationEvent.collect { event ->
                            when (event) {
                                is VerificationScreen -> {
                                    navController.navigate(
                                        VerificationScreen(
                                            event.phoneNumber,
                                            event.userToken
                                        )
                                    )
                                }

                                else -> {

                                }
                            }
                        }
                    }

                }

            )
        }
        composable<VerificationScreen> { navBackStackEntry ->
            val phoneVerificationViewModel: PhoneVerificationViewModel = hiltViewModel()
            val state = phoneVerificationViewModel.uiState.collectAsStateWithLifecycle().value
            val screenArgs = navBackStackEntry.toRoute<VerificationScreen>()
            PhoneVerificationScreen(
                modifier = Modifier.padding(innerPadding),
                state = state,
                onCodeValueChange = { index, value ->
                    phoneVerificationViewModel.onCodeValueChange(index, value)
                },
                onResendCodeClick = { phoneVerificationViewModel.onResendCode() },
                onNextClick = {
                    phoneVerificationViewModel.onNextClick(
                        navigate = {
                            navController.navigate(InterestSelectionScreen(screenArgs.userToken))
                        }
                    )
                },
                phoneNumber = screenArgs.phoneNumber
            )

        }

        composable<InterestSelectionScreen> { backStackEntry ->
            val interestSelectionViewModel: InterestSelectionViewModel = hiltViewModel()
            val screenArgs = backStackEntry.toRoute<InterestSelectionScreen>()
            val state =
                interestSelectionViewModel.interestUIState.collectAsStateWithLifecycle().value
            val selectedLanguage = interestSelectionViewModel.getSelectedLanguage()
            InterestSelectionScreen(
                modifier = Modifier.padding(innerPadding),
                state = state,
                onBackClick = { navController.popBackStack() },
                onInterestSelected = { selectedInterest ->
                    interestSelectionViewModel.onInterestSelected(selectedInterest)
                },
                selectedLang = selectedLanguage,
                onNextClick = {
                    navController.navigate(
                        SportSelectionScreen(
                            state.selectedInterestId ?: 0,
                            selectedLanguage,
                            screenArgs.userToken
                        )
                    )
                }
            )

        }
        composable<SportSelectionScreen> { backStackEntry ->
            val sportSelectionViewModel: SportSelectionViewModel = hiltViewModel()
            val state = sportSelectionViewModel.uiState.collectAsStateWithLifecycle().value
            val screenArg = backStackEntry.toRoute<SportSelectionScreen>()
            val selectedInterestId = screenArg.interestId
            val selectedLang = screenArg.selectedLang
            SportSelectionScreen(
                state = state,
                modifier = Modifier.padding(innerPadding),
                onBackClick = { navController.popBackStack() },
                selectSport = { id ->
                    sportSelectionViewModel.selectSport(id)
                },
                onNextClick = {
                    sportSelectionViewModel.updateInterestsAndSports(
                        InterestsUpdateRequest(
                            listOf(selectedInterestId),
                            listOf(state.selectedSportId ?: 0)
                        ),
                        screenArg.userToken.toString()
                    )
                },
                selectedLang = selectedLang,

                onUpdatingSportAndInterestSuccess = {
                    LaunchedEffect(Unit) {
                        sportSelectionViewModel.navigationEvent.collect { event ->
                            when (event) {
                                is LoginScreen ->
                                    navController.navigate(LoginScreen) {
                                        popUpTo(0) { inclusive = true }
                                    }

                                else -> {
                                }
                            }
                        }
                    }

                }
            )
        }
    }
}
