package com.metafortech.calma

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.metafortech.calma.presentation.authentication.authNav
import com.metafortech.calma.presentation.theme.CalmaTheme
import com.metafortech.calma.presentation.welcom.LanguageScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.graphics.Color
import com.metafortech.calma.presentation.AppRoute.AuthNav
import com.metafortech.calma.presentation.AppRoute.HomeNav
import com.metafortech.calma.presentation.AppRoute.LanguageScreen
import com.metafortech.calma.presentation.home.ConditionalScaffold
import com.metafortech.calma.presentation.home.homeNav
import com.metafortech.calma.presentation.home.rememberAppInitState
import com.metafortech.calma.presentation.welcom.LanguageScreenViewModel
import com.metafortech.calma.presentation.welcom.LocaleHelper

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var languageScreenViewModel: LanguageScreenViewModel

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("app_settings", MODE_PRIVATE)
        val lang = prefs.getString("selected_language", "en") ?: "en"
        val contextWithLocale = LocaleHelper.wrapContextWithLocale(newBase, lang)
        super.attachBaseContext(contextWithLocale)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        languageScreenViewModel = viewModels<LanguageScreenViewModel>().value
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                Color.White.hashCode(),
                Color.Transparent.hashCode()
            )
        )

        setContent {
            val navController = rememberNavController()
            val (isRegistered, isLoggedIn, userImageUrl) = rememberAppInitState()
            val startDestination = when {
                isLoggedIn -> HomeNav()
                isRegistered -> AuthNav
                else -> LanguageScreen
            }
            CalmaTheme {
                ConditionalScaffold(
                    navController,
                    userImageUrl,
                    isLoggedIn
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable<LanguageScreen> {
                            LanguageScreen(
                                modifier = Modifier.padding(innerPadding)
                            ) { languageTag ->
                                onUserSelectedLanguage(languageTag)
                                navController.navigate(AuthNav)
                            }
                        }
                        authNav(innerPadding, navController)
                        homeNav(innerPadding, navController)
                    }
                }
            }
        }
    }

    private fun onUserSelectedLanguage(languageTag: String) {
        if (languageScreenViewModel.isLocaleChanged(languageTag)) {
            languageScreenViewModel.setAppLocale(languageTag)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}


