package com.metafortech.calma

import android.app.LocaleManager
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.core.os.LocaleListCompat
import java.util.Locale

class LocaleManager(private val context: Context) {

    fun setAppLocale(languageTag: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList(Locale.forLanguageTag(languageTag))
        } else {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageTag)
            AppCompatDelegate.setApplicationLocales(appLocale)

            val locale = Locale(languageTag)
            Locale.setDefault(locale)
            val config = context.resources.configuration
            config.setLocale(locale)
        }
    }

    fun getSavedLocale(): String {
        return context.getSharedPreferences("app_settings", MODE_PRIVATE)
            .getString("selected_language", "en") ?: "en"
    }

    fun saveLocale(languageTag: String) {
        context.getSharedPreferences("app_settings", MODE_PRIVATE)
            .edit { putString("selected_language", languageTag) }
    }

    fun isLocaleChanged(newLanguageTag: String): Boolean {
        val currentLanguage = getSavedLocale()
        return currentLanguage != newLanguageTag
    }
}