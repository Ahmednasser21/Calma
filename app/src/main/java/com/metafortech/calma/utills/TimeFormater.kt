package com.metafortech.calma.utills

import android.annotation.SuppressLint
import android.content.Context
import com.metafortech.calma.R
import jakarta.inject.Inject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    fun formatTimestamp(timestamp: Long, context: Context): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60000 -> context.getString(R.string.just_now)
            diff < 3600000 -> "${diff / 60000} " + context.getString(R.string.minute)
            diff < 86400000 -> "${diff / 3600000} " + context.getString(R.string.hour)
            diff < 604800000 -> "${diff / 86400000} " + context.getString(R.string.day)
            else -> {
                val date = SimpleDateFormat("MMM dd", Locale.getDefault())
                date.format(Date(timestamp))
            }
        }
    }
}