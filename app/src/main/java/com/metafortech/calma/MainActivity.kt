package com.metafortech.calma

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.metafortech.calma.authentication.AuthNav
import com.metafortech.calma.authentication.authNav
import com.metafortech.calma.theme.CalmaTheme
import com.metafortech.calma.welcom.LocaleManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            val navController = rememberNavController()
            CalmaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(navController = navController, startDestination = AuthNav) {
                        authNav(innerPadding, navController) { languageTag ->
                            onUserSelectedLanguage(languageTag)
                        }
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
