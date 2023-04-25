package com.example.mebby.app.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import com.example.mebby.R
import com.example.mebby.app.functions.getOutputMediaFileUri
import com.example.mebby.app.observers.ImagesObserver
import com.google.android.material.bottomsheet.BottomSheetDialog

class ImagesBottomSheetDialog(
    private val context: Context,
    private val imagesObserver: ImagesObserver
): View.OnClickListener {
    private val bottomSheetDialog by lazy {
        val dialog = BottomSheetDialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = LayoutInflater.from(context).inflate(R.layout.add_photo_bottom_sheet_dialog_layout, null, false)
        dialog.setContentView(view)

        view.findViewById<LinearLayout>(R.id.camera_linear_layout)?.setOnClickListener(this)
        view.findViewById<LinearLayout>(R.id.gallery_linear_layout)?.setOnClickListener(this)

        dialog
    }

    fun init() {
        bottomSheetDialog.show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.camera_linear_layout -> {
                imagesObserver.takePhoto()
                bottomSheetDialog.dismiss()
            }
            R.id.gallery_linear_layout -> {
                imagesObserver.selectImageFromGallery()
                bottomSheetDialog.dismiss()
            }
        }
    }
}

