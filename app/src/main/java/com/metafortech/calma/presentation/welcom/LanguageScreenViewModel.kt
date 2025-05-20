package com.metafortech.calma.presentation.welcom

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import com.metafortech.calma.data.local.AppPreferences

@HiltViewModel
class LanguageScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appPreferences: AppPreferences
) : ViewModel() {


    private val _currentLanguage = MutableStateFlow(getSavedLocale())
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun setAppLocale(languageTag: String) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                context.getSystemService(android.app.LocaleManager::class.java)?.applicationLocales =
                    android.os.LocaleList(Locale.forLanguageTag(languageTag))
            }

            else -> {
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageTag)
                AppCompatDelegate.setApplicationLocales(appLocale)

                val locale = Locale(languageTag)
                Locale.setDefault(locale)
                val config = context.resources.configuration
                config.setLocale(locale)
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            }
        }

        saveLocale(languageTag)
        _currentLanguage.value = languageTag
    }

    private fun getSavedLocale(): String {
        return appPreferences.getString("selected_language", "en")
    }

    private fun saveLocale(languageTag: String) {
        appPreferences.saveString("selected_language", languageTag)
    }

    fun isLocaleChanged(newLanguageTag: String): Boolean {
        return _currentLanguage.value != newLanguageTag
    }
}
