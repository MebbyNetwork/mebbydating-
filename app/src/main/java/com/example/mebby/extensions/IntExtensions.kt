package com.example.mebby.extensions

import android.content.res.Resources

fun Int.convertPxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.convertDpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}