package com.metafortech.calma.utills

import android.annotation.SuppressLint
import jakarta.inject.Inject

class TimeFormater @Inject constructor() {

    @SuppressLint("DefaultLocale")
    fun formatTime(timeInMillis: Long): String {
        val seconds = (timeInMillis / 1000) % 60
        val minutes = (timeInMillis / (1000 * 60)) % 60
        val hours = (timeInMillis / (1000 * 60 * 60))
        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%d:%02d", minutes, seconds)
        }
    }
}