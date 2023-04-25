package com.example.mebby.app.ui.profile

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mebby.R
import com.example.mebby.app.adapters.profileAdapter.ProfileInterestAdapter
import com.example.mebby.app.adapters.profileAdapter.ProfilePhotogalleryAdapter
import com.example.mebby.app.decorators.InterestsItemDecorator
import com.example.mebby.app.decorators.ProfilePhotogalleryImageDecorator
import com.example.mebby.app.dialogs.ImagesBottomSheetDialog
import com.example.mebby.app.observers.ImagesObserver
import com.example.mebby.app.viewModels.ProfileViewModel
import com.example.mebby.const.EDIT_PROFILE
import com.example.mebby.const.USER
import com.example.mebby.data.Resource
import com.example.mebby.databinding.FragmentProfileBinding
import com.example.mebby.domain.models.ImageModel
import com.example.mebby.domain.models.UserProfileModel
import com.example.mebby.extensions.convertDpToPx
import com.example.mebby.extensions.getAge
import com.example.mebby.extensions.navigateSafe
import com.google.android.flexbox.FlexboxLayoutManager
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm by activityViewModels<ProfileViewModel>()

    private lateinit var imagesObserver: ImagesObserver

    //InterestRecyclerViewAdapter
    private val profileInterestsAdapter by lazy { ProfileInterestAdapter() }

    //PhotogalleryRecyclerViewAdapter
    private val profilePhotogalleryAdapter by lazy {
        val metrics =
            (Resources.getSystem().displayMetrics.widthPixels - (Integer.valueOf(32).convertDpToPx())) / 3

        Log.d("PhotogalleryRecyclerViewAdapter", "$metrics")

        ProfilePhotogalleryAdapter(metrics, context = requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(EDIT_PROFILE) { requestKey, bundle ->
            if (requestKey != EDIT_PROFILE) return@setFragmentResultListener

            val result = bundle.getBoolean(USER)

            if (result) {
                vm.getUser()
            }
        }

        imagesObserver = ImagesObserver(
            registry = requireActivity().activityResultRegistry,
            context = requireContext(),
            actionListener = object : com.example.mebby.app.observers.ActionListener {
                override fun addImage(imageModel: ImageModel) {
                    vm.addImage(imageModel)
                }

            })
        lifecycle.addObserver(imagesObserver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        initInterestsRecyclerView()
        initPhotogalleryRecyclerView()

        binding.settingsButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(USER, vm.user.value?.data)
            findNavController().navigateSafe(R.id.action_profileFragment_to_settingsFragment, bundle)
        }

        binding.editProfileButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(USER, vm.user.value?.data)
            findNavController().navigateSafe(R.id.action_profileFragment_to_editProfile, bundle)
        }

        binding.buttonAddPhoto.setOnClickListener {
            ImagesBottomSheetDialog(requireContext(), imagesObserver).init()
        }

        userProfileModel()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun userProfileModel() {
        // Observe user data from view model
        vm.user.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        with(it.data) {
                            // Initialize user data in UI
                            initUser(this!!)

                            binding.profile.visibility = View.VISIBLE
                            binding.shimmerProfile.visibility = View.GONE
                        }
                    }
                }
                is Resource.Error -> {
                    Log.d("userFromFirestore", "${it.message}")
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUser(user: UserProfileModel) {
        // Load profile image using Picasso library
        Picasso.get().load(user.generalImage).placeholder(R.color.shimmer).into(binding.profileImage)

        // Set about text if available, otherwise hide the view
        if (user.about!!.isNotEmpty()) {
            binding.about.text = user.about
        } else {
            binding.about.visibility = View.GONE
        }

        // Set profile name and age
        binding.profileName.text = resources.getString(R.string.name_and_age, user.username, user.birthday.getAge())

        // Set verification visibility based on KYC status
        binding.verification.visibility = if (user.KYC) View.VISIBLE else View.GONE

        // Set profile city
        binding.profileCity.text = "${user.city.city}, ${user.city.country}"

        // Set interests in interests recycler view
        profileInterestsAdapter.setInterests(user.interests)

        // Set photogallery images in photogallery recycler view
        user.profileImage?.let { profilePhotogalleryAdapter.setPhotogallery(it.sortedBy { it.timestamp }.reversed()) }
    }

    private fun initInterestsRecyclerView() {
        val recyclerView = binding.interestsRecyclerView

        recyclerView.layoutManager = FlexboxLayoutManager(requireContext())
        recyclerView.addItemDecoration(InterestsItemDecorator(16, 16))
        recyclerView.adapter = profileInterestsAdapter
    }

    private fun initPhotogalleryRecyclerView() {
        val recyclerView = binding.photogalleryRecyclerView

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = profilePhotogalleryAdapter
        recyclerView.addItemDecoration(ProfilePhotogalleryImageDecorator())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.removeObserver(imagesObserver)
        _binding = null
    }
}