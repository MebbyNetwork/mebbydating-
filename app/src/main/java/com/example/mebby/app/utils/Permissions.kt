package com.example.mebby.app.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mebby.const.ACTIVITY
import com.example.mebby.const.REQUEST_PERMISSION_SELECT_PHOTO_IN_GALLERY
import com.example.mebby.const.REQUEST_PERMISSION_TAKE_PHOTO

interface PermissionsInterface {
    fun startActivity(intent: Intent, requestCode: Int)
    fun requestPermission(array: Array<String>, RequestCode: Int)
}

class Permissions(private val permissionsInterface: PermissionsInterface) {
    fun checkCameraPermission(intent: Intent?, requestCode: Int, context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permissionsInterface.startActivity(intent!!, requestCode)
        } else {
            permissionsInterface.requestPermission(arrayOf(Manifest.permission.CAMERA), REQUEST_PERMISSION_TAKE_PHOTO)
        }
    }

    fun checkGalleryPermission(intent: Intent?, requestCode: Int, context: Context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permissionsInterface.startActivity(intent!!, requestCode)
        } else {
            permissionsInterface.requestPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION_SELECT_PHOTO_IN_GALLERY)
        }
    }
}