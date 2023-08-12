package com.example.mebby.app.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun getOutputMediaFileUri(context: Context): Uri = FileProvider.getUriForFile(context, "com.example.mebby.provider", getOutputMediaFile())

private fun getOutputMediaFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "IMAGE$timeStamp.jpg"
    val mediaStoreDirection = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    return File(mediaStoreDirection.absolutePath + File.separator + imageFileName)
}
