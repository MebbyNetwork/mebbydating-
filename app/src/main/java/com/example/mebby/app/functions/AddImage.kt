package com.example.mebby.app.functions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.mebby.const.ACTIVITY
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("QueryPermissionsNeeded")
fun selectImageInGallery(): Intent? {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.type = "image/*"

    return if (intent.resolveActivity(ACTIVITY.packageManager) != null) intent else null
}

@SuppressLint("QueryPermissionsNeeded")
fun takePhoto(context: Context, callback: (Uri) -> Unit): Intent? {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val fileUri =  getOutputMediaFileUri(context)

    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

    callback(fileUri)

    return if (intent.resolveActivity(ACTIVITY.packageManager) != null) intent else null
}

fun getOutputMediaFileUri(context: Context) = FileProvider.getUriForFile(context, "com.example.mebby.provider", getOutputMediaFile())

private fun getOutputMediaFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "IMAGE$timeStamp.jpg"
    val mediaStoreDirection = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

    return File(mediaStoreDirection.absolutePath + File.separator + imageFileName)
}
