package com.metafortech.calma

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.metafortech.calma.authentication.AuthNav
import com.metafortech.calma.authentication.authNav
import com.metafortech.calma.theme.CalmaTheme
import com.metafortech.calma.welcom.LanguageScreen
import com.metafortech.calma.welcom.LocaleManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var localeManager: LocaleManager
    override fun onCreate(savedInstanceState: Bundle?) {
        localeManager = LocaleManager(this)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            localeManager.setAppLocale(localeManager.getSavedLocale())
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            CalmaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = WelcomeScreen) {
                        composable<WelcomeScreen> {
                            LanguageScreen(
                                modifier = Modifier.padding(innerPadding)
                            ) { languageTag ->
                                onUserSelectedLanguage(languageTag)
                                navController.navigate(AuthNav)
                            }
                        }
                        authNav(innerPadding, navController)
                    }
                }
            }
        }
    }

    private fun onUserSelectedLanguage(languageTag: String) {
        if (localeManager.isLocaleChanged(languageTag)) {
            localeManager.saveLocale(languageTag)
            localeManager.setAppLocale(languageTag)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                finishAffinity()
                startActivity(intent)
            }
        }
    }
}

@Serializable
object WelcomeScreen
