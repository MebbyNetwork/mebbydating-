package com.example.mebby.app.observers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.mebby.app.functions.getOutputMediaFileUri
import com.example.mebby.const.REQUEST_SELECT_PHOTO_IN_GALLERY
import com.example.mebby.const.REQUEST_TAKE_PHOTO
import com.example.mebby.domain.models.ImageModel

interface ActionListener {
    fun addImage(imageModel: ImageModel)
}

class ImagesObserver(
    private val context: Context,
    private val registry: ActivityResultRegistry,
    private val actionListener: ActionListener,
    ) : DefaultLifecycleObserver {
    private lateinit var getContent : ActivityResultLauncher<String>
    private lateinit var takePicture : ActivityResultLauncher<Uri>

    private lateinit var requestGalleryPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var requestCameraPermissionLauncher: ActivityResultLauncher<String>

    private var imageUri: Uri? = null

    override fun onCreate(owner: LifecycleOwner) {
        getContent = registry.register("selectImageFromGallery", owner, ActivityResultContracts.GetContent()) {
            if (it != null) {
                val image = ImageModel(
                    request = REQUEST_SELECT_PHOTO_IN_GALLERY.toLong(),
                    generalImage = false,
                    uri = it.toString()
                )

                actionListener.addImage(image)
            }
        }

        takePicture = registry.register("takePicture", owner, ActivityResultContracts.TakePicture()) {
            if (it && imageUri != null) {
                val image = ImageModel(
                    request = REQUEST_TAKE_PHOTO.toLong(),
                    generalImage = false,
                    uri = imageUri.toString()
                )

                actionListener.addImage(image)
            }
        }

        requestGalleryPermissionLauncher = registry.register("requestGalleryPermissionLauncher", owner, ActivityResultContracts.RequestPermission()) { _ ->
            when {
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                    getContent.launch("image/*")
                }

                !shouldShowRequestPermissionRationale(Activity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    Toast.makeText(context, "Please, took it", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(context, "Repeat", Toast.LENGTH_SHORT).show()
                }
            }
        }

        requestCameraPermissionLauncher = registry.register("requestCameraPermissionLauncher", owner, ActivityResultContracts.RequestPermission()) { _ ->
            when {
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                    imageUri = getOutputMediaFileUri(context)
                    takePicture.launch(imageUri)
                }

                !shouldShowRequestPermissionRationale(Activity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    Toast.makeText(context, "Please, took it", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(context, "Repeat", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun selectImageFromGallery() {
        requestGalleryPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun takePhoto() {
        requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

}