package com.example.mebby.extensions

import android.util.Log

fun Long.getAge(): Int {
    return this.calculateYearsSinceTimestamp()
}

fun Long.calculateYearsSinceTimestamp(): Int {
    return try {
        val currentDateMillis = System.currentTimeMillis()
        val years = ((currentDateMillis - this) / (1000L * 60 * 60 * 24 * 365.25)).toInt()
        years
    } catch (e: Exception) {
        Log.d("Long.convertTimestampToDate()", e.localizedMessage ?: "Something went wrong")
        0
    }
}