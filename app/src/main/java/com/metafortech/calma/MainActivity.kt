package com.metafortech.calma

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalmaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LanguageScreen(modifier = Modifier.padding(innerPadding), onLanguageClick = {languageTag->

                    })
                }
            }
        }
    }
}
