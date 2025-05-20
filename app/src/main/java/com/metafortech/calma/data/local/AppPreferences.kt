package com.metafortech.calma.data.local

interface AppPreferences {
    fun saveString(key: String, value: String)
    fun getString(key: String, defaultValue: String = ""): String
    fun saveInt(key: String, value: Int)
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun remove(key: String)
    fun clear()
}