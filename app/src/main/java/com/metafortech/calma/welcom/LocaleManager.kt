package com.metafortech.calma.welcom

import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import java.util.Locale

class LocaleManager(private val context: Context) {

    fun setAppLocale(languageTag: String) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                context.getSystemService(android.app.LocaleManager::class.java)?.applicationLocales =
                    LocaleList(Locale.forLanguageTag(languageTag))
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
    }

    fun getSavedLocale(): String {
        return context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            .getString("selected_language", "en") ?: "en"
    }

    fun saveLocale(languageTag: String) {
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            .edit { putString("selected_language", languageTag) }
    }

    fun isLocaleChanged(newLanguageTag: String): Boolean {
        val currentLanguage = getSavedLocale()
        return currentLanguage != newLanguageTag
    }
}