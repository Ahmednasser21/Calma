package com.metafortech.calma.presentation.welcom

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    fun wrapContextWithLocale(base: Context, languageTag: String): Context {
        val locale = Locale.forLanguageTag(languageTag)
        Locale.setDefault(locale)

        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }
}
