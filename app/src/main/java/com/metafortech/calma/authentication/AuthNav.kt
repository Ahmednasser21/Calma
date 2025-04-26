package com.metafortech.calma.authentication

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.metafortech.calma.WelcomeScreen
import com.metafortech.calma.authentication.login.presentation.LoginScreen
import com.metafortech.calma.authentication.login.presentation.LoginViewModule
import com.metafortech.calma.authentication.register.presentation.RegisterScreen
import com.metafortech.calma.authentication.register.presentation.RegisterViewModule
import kotlinx.serialization.Serializable


fun NavGraphBuilder.authNav(
    innerPadding: PaddingValues,
    navController: NavHostController,
) {
    navigation<AuthNav>(startDestination = LoginScreen) {

        composable<LoginScreen> {
            val loginViewModel: LoginViewModule = hiltViewModel()
            val state = loginViewModel.uiState.collectAsState().value
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
            val registerViewModel: RegisterViewModule = hiltViewModel()
            val registerState = registerViewModel.uiState.collectAsState().value
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
                onLoginWithGoogleClick = { registerViewModel.onLoginWithGoogleClick(it) },
                onLoginWithFacebookClick = { registerViewModel.onLoginWithFacebookClick() }

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