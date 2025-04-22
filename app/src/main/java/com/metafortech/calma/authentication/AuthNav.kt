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
import com.metafortech.calma.authentication.login.presentation.LoginScreen
import com.metafortech.calma.authentication.login.presentation.LoginViewModule
import com.metafortech.calma.authentication.register.presentation.RegisterScreen
import com.metafortech.calma.welcom.LanguageScreen
import kotlinx.serialization.Serializable


fun NavGraphBuilder.authNav(
    innerPadding: PaddingValues,
    navController: NavHostController,
    onUserSelectedLanguage: (String) -> Unit
) {
    navigation<AuthNav>(startDestination = WelcomeScreen) {
        composable<WelcomeScreen> {
            LanguageScreen(
                modifier = Modifier.padding(innerPadding)
            ) { languageTag ->
                onUserSelectedLanguage(languageTag)
                navController.navigate(LoginScreen)
            }
        }
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
                onRegisterClick = { navController.navigate(RegisterScreen) }
            )
        }
        composable<RegisterScreen> {
            RegisterScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Serializable
object AuthNav

@Serializable
object WelcomeScreen

@Serializable
object LoginScreen

@Serializable
object RegisterScreen