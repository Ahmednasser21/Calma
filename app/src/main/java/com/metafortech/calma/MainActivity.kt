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
import com.metafortech.calma.ui.theme.CalmaTheme
import com.metafortech.calma.ui.welcom.LanguageScreen

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
                    LanguageScreen(
                        modifier = Modifier.padding(innerPadding)
                    ) { languageTag ->
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


