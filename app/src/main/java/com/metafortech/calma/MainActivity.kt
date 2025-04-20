package com.metafortech.calma

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.metafortech.calma.login.presentation.LoginScreen
import com.metafortech.calma.theme.CalmaTheme
import com.metafortech.calma.welcom.LanguageScreen
import com.metafortech.calma.welcom.LocaleManager
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    private lateinit var localeManager: LocaleManager

    override fun onCreate(savedInstanceState: Bundle?) {
        localeManager = LocaleManager(this)
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
            localeManager.setAppLocale(localeManager.getSavedLocale())
        }

        enableEdgeToEdge()
        setContent {
            CalmaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Nav(innerPadding){languageTag ->
                        onUserSelectedLanguage(languageTag)
                    }
                }
            }
        }
    }

    private fun onUserSelectedLanguage(languageTag: String) {
        if (localeManager.isLocaleChanged(languageTag)) {
            localeManager.saveLocale(languageTag)
            localeManager.setAppLocale(languageTag)

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                finishAffinity()
                startActivity(Intent(this, MainActivity::class.java))
                return
            }
        }
    }
}

@Composable
fun Nav(innerPadding: PaddingValues, onUserSelectedLanguage: (String) -> Unit) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WelcomeScreen) {
        composable<WelcomeScreen> {
            LanguageScreen(
                modifier = Modifier.padding(innerPadding)
            ) { languageTag ->
                onUserSelectedLanguage(languageTag)
                navController.navigate(LoginScreen)
            }
        }
        composable<LoginScreen> {
            LoginScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Serializable
object WelcomeScreen

@Serializable
object LoginScreen



