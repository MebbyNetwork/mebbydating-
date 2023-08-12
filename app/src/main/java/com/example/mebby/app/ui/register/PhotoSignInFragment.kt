package com.example.mebby.app.ui.register
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.models.PictureModel
import com.example.mebby.R
import com.example.mebby.app.adapters.imageAdapter.ActionListener
import com.example.mebby.app.adapters.imageAdapter.PhotoRecyclerViewAdapter
import com.example.mebby.app.decorators.SpacingItemDecorator
import com.example.mebby.app.dialogs.ImagesBottomSheetDialog
import com.example.mebby.app.observers.ImagesObserver
import com.example.mebby.app.utils.*
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.databinding.FragmentPhotoSignInBinding


class PhotoSignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentPhotoSignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: RegistrationViewModel by activityViewModels()

    // ImageObserver
    private lateinit var imagesObserver: ImagesObserver

    //RecyclerView
    private val addPhotoRecyclerViewAdapter by lazy { PhotoRecyclerViewAdapter(object : ActionListener {
        override fun addPicture() {
            ImagesBottomSheetDialog(requireContext(), imagesObserver).init()
        }

        override fun deletePicture(picture: PictureModel) {
            deletePhotoInc(picture)
        }
    })
    }

    //ItemTouchHelper
    private val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(
        ItemTouchHelperCallbacks(object : ItemTouchHelperActionListener {
            override fun move(sourcePosition: Int, targetPosition: Int) {
                vm.swapPicture(sourcePosition, targetPosition)
            }
        })
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imagesObserver = ImagesObserver(
            registry = requireActivity().activityResultRegistry,
            context = requireContext(),
            actionListener = object : com.example.mebby.app.observers.ActionListener() {
                override fun addPicture(picture: PictureModel) {
                    Log.d("PhotoUri", "$picture")
                    if (vm.images.value?.contains(picture) == true) {
                        Toast.makeText(requireContext(), "This photo already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        vm.addPictureInList(picture)
                    }
                }
            })
        lifecycle.addObserver(imagesObserver)
    }

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

    //Removes photo from viewModel List<Photo>
    private fun deletePhotoInc(picture: PictureModel) {
        Log.d("PhotoUri", "$picture")
        vm.deletePictureFromList(picture)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

