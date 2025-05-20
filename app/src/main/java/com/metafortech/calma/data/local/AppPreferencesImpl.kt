package com.metafortech.calma.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesImpl @Inject constructor(@ApplicationContext context: Context) : AppPreferences {

    companion object {
        private const val PREF_FILE_NAME = "app_settings"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    override fun saveString(key: String, value: String) {
        preferences.edit { putString(key, value) }
    }

    override fun getString(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue) ?: defaultValue
    }

    override fun saveInt(key: String, value: Int) {
        preferences.edit { putInt(key, value) }
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    override fun saveBoolean(key: String, value: Boolean) {
        preferences.edit { putBoolean(key, value) }
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    override fun remove(key: String) {
        preferences.edit { remove(key) }
    }

    override fun clear() {
        preferences.edit { clear() }
    }
}