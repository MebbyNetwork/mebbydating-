package com.example.mebby.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import com.example.mebby.const.DATE_PATTERN
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
fun checkYears(date: String): Boolean {
    val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
    val parsedDate = LocalDate.parse(date, dateFormatter)
    val currentDate = LocalDate.now()
    val age = currentDate.year - parsedDate.year

    return age in 18..100
}

@SuppressLint("SimpleDateFormat")
fun getTimestamp(date: String): Timestamp {
    val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    val parsedDate = dateFormat.parse(date)
    val timestamp = parsedDate?.time ?: 0L

    return Timestamp(timestamp)
}

fun validateDate(date: String): Boolean {
    val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    dateFormat.isLenient = false

    return try {
        dateFormat.parse(date)
        true
    } catch (e: Exception) {
        false
    }
}

fun Long.convertTimestampToDate(): String {
    val formatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    return formatter.format(Date(this))
}

