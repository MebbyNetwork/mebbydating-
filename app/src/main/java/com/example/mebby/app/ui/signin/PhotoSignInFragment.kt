package com.example.mebby.app.ui.signin

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mebby.R
import com.example.mebby.app.adapters.imageAdapter.AddPhotoActionListener
import com.example.mebby.app.adapters.imageAdapter.PhotoRecyclerViewAdapter
import com.example.mebby.app.decorators.SpacingItemDecorator
import com.example.mebby.app.functions.selectImageInGallery
import com.example.mebby.app.functions.takePhoto
import com.example.mebby.app.utils.*
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.const.REQUEST_PERMISSION_SELECT_PHOTO_IN_GALLERY
import com.example.mebby.const.REQUEST_PERMISSION_TAKE_PHOTO
import com.example.mebby.const.REQUEST_TAKE_PHOTO
import com.example.mebby.const.REQUEST_SELECT_PHOTO_IN_GALLERY
import com.example.mebby.databinding.FragmentPhotoSignInBinding
import com.example.mebby.domain.models.ImageModel
import com.google.android.material.bottomsheet.BottomSheetDialog


class PhotoSignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentPhotoSignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: RegistrationViewModel by activityViewModels()

    //PhotoUri for onActivityResult
    private var photoUri: Uri? = null

    //RecyclerView
    private var addPhotoRecyclerViewAdapter = PhotoRecyclerViewAdapter(object : AddPhotoActionListener {
        override fun addPhoto() {
            initBottomSheetDialog()
        }

        override fun deletePhoto(imageModel: ImageModel) {
            deletePhotoInc(imageModel)
        }

    })

    //ItemTouchHelper
    private val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(
        ItemTouchHelperCallbacks(object : ItemTouchHelperActionListener {
            override fun move(sourcePosition: Int, targetPosition: Int) {
                vm.swapImageInList(sourcePosition, targetPosition)
            }
        })
    )

    //Permissions
    private val permissions = Permissions(object : PermissionsInterface {
        override fun startActivity(intent: Intent, requestCode: Int) {
            startActivityForResult(intent, requestCode)
        }

        override fun requestPermission(array: Array<String>, requestCode: Int) {
            requestPermissions(array, requestCode)
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _binding = FragmentPhotoSignInBinding.inflate(inflater, container, false)

        //Initializes PhotoRecyclerView
        initPhotoRecyclerView()

        vm.images.observe(viewLifecycleOwner) {
            //Update List<Photo?> in RecyclerViewAdapter
            addPhotoRecyclerViewAdapter.setList(it)

            //Allow to go to the next step of registration if the count photos >= 2
            binding.buttonContinue.isEnabled = it.size >= 2

            //Updates text for @button_continue
            binding.buttonContinue.text = resources.getString(R.string.continue_text_with_counter, it.size, 6)

        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonContinue.setOnClickListener {
            findNavController().navigate(R.id.action_photoSignInFragment_to_interestsSignInFragment)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                REQUEST_TAKE_PHOTO -> {
                    if ((photoUri) != null) {
                        addPhotoInc(ImageModel(
                            request = requestCode.toLong(),
                            uri = photoUri.toString()
                        ))
                    }
                    photoUri = null
                }

                REQUEST_SELECT_PHOTO_IN_GALLERY -> {
                    addPhotoInc(imageModel = ImageModel(
                        request = requestCode.toLong(),
                        uri = data?.data.toString()
                    ))
                }

            }

        } else {
            Toast.makeText(requireContext(), "Cancel", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Log.d("MEBBYDATING", "$requestCode")
        when (requestCode) {
            REQUEST_PERMISSION_TAKE_PHOTO -> {
                Log.d("MEBBYDATING", "$grantResults")
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(takePhoto(requireContext()) { photoUri = it }, REQUEST_TAKE_PHOTO)
                }
            }

            REQUEST_PERMISSION_SELECT_PHOTO_IN_GALLERY -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(selectImageInGallery(), REQUEST_SELECT_PHOTO_IN_GALLERY)
                }
            }
        }
    }

    //Removes photo from viewModel List<Photo>
    private fun deletePhotoInc(imageModel: ImageModel) {
        Log.d("PhotoUri", "${imageModel}")
        vm.deleteImageInList(imageModel)
    }

    //Add photo to the viewModel List<Photo>
    private fun addPhotoInc(imageModel: ImageModel) {
        Log.d("PhotoUri", "${imageModel}")
        if (vm.images.value?.contains(imageModel) == true) {
            Toast.makeText(requireContext(), "This photo already exists", Toast.LENGTH_SHORT).show()
        } else {
            vm.addImageInList(imageModel)
        }
    }

    //Method initializes PhotoRecyclerView. Are assigned to him LayoutManager, adapter, ItemDecorator and ItemTouchHelper
    private fun initPhotoRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 3)
        val recyclerView = binding.imageViewRecyclerView

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = addPhotoRecyclerViewAdapter
        recyclerView.addItemDecoration(SpacingItemDecorator(horizontalSpaceHeight = 8, verticalSpaceHeight = 8, spanCount = 3))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    //Method calls BottomSheetDialog to select how to add photos
    private fun initBottomSheetDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        bottomSheetDialog.setContentView(R.layout.add_photo_bottom_sheet_dialog_layout)

        val cameraLinearLayout = bottomSheetDialog.findViewById<LinearLayout>(R.id.camera_linear_layout)
        val galleryLinearLayout = bottomSheetDialog.findViewById<LinearLayout>(R.id.gallery_linear_layout)

        cameraLinearLayout?.setOnClickListener {
            permissions.checkCameraPermission(takePhoto(requireContext()) { photoUri = it }, REQUEST_TAKE_PHOTO, requireContext())
            bottomSheetDialog.dismiss()
        }
        galleryLinearLayout?.setOnClickListener {
            permissions.checkGalleryPermission(selectImageInGallery(), REQUEST_SELECT_PHOTO_IN_GALLERY, requireContext())
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

}

