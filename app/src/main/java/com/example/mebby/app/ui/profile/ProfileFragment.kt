package com.example.mebby.app.ui.profile

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.domain.Resource
import com.example.domain.models.PictureModel
import com.example.domain.models.ProfileModel
import com.example.mebby.R
import com.example.mebby.app.adapters.profileAdapter.PhotogalleryActionListener
import com.example.mebby.app.adapters.profileAdapter.ProfileInterestAdapter
import com.example.mebby.app.adapters.profileAdapter.ProfilePhotogalleryAdapter
import com.example.mebby.app.customViews.OverlayPicturesView
import com.example.mebby.app.decorators.InterestsItemDecorator
import com.example.mebby.app.decorators.ProfilePhotogalleryImageDecorator
import com.example.mebby.app.dialogs.ImagesBottomSheetDialog
import com.example.mebby.app.observers.ImagesObserver
import com.example.mebby.app.viewModels.ProfileViewModel
import com.example.mebby.const.EDIT_PROFILE
import com.example.mebby.const.USER
import com.example.mebby.databinding.FragmentProfileBinding
import com.example.mebby.extensions.convertDpToPx
import com.example.mebby.extensions.getAge
import com.example.mebby.extensions.navigateSafe
import com.example.mebby.extensions.setStatusBarColor
import com.google.android.flexbox.FlexboxLayoutManager
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    private val profileInterestsAdapter by lazy {
        ProfileInterestAdapter()
    }

    //PhotogalleryRecyclerViewAdapter
    private val profilePhotogalleryAdapter by lazy {
        val metrics = (Resources.getSystem().displayMetrics.widthPixels - (Integer.valueOf(32)
            .convertDpToPx())) / 3
        ProfilePhotogalleryAdapter(metrics,
            context = requireContext(),
            object : PhotogalleryActionListener {
                override fun showPicture(position: Int, imageView: ImageView) {
                    showGallery(position, imageView, OverlayPicturesView(requireContext()))
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(EDIT_PROFILE) { requestKey, bundle ->
            if (requestKey != EDIT_PROFILE) return@setFragmentResultListener
            if (bundle.getBoolean(USER)) vm.getUser()
        }


        imagesObserver = ImagesObserver(
            registry = requireActivity().activityResultRegistry, context = requireContext(),
            actionListener = object : com.example.mebby.app.observers.ActionListener() {
                override fun addPicture(picture: PictureModel) {
                    vm.addPicture(picture)
                }
            })
        lifecycle.addObserver(imagesObserver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        initInterestsRecyclerView()
        initPhotogalleryRecyclerView()

        // Observe user data from view model
        vm.user.observe(viewLifecycleOwner) { it ->
            when (it) {
                is Resource.Success -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        it.data?.let { profile -> showProfile(profile) }
                    }

                    binding.profile.visibility = View.VISIBLE
                    binding.shimmerProfile.visibility = View.GONE
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "${it.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
                is Resource.Loading -> {
                    binding.shimmerProfile.visibility = View.VISIBLE
                    binding.profile.visibility = View.GONE
                }
            }
        }

        binding.settingsButton.setOnClickListener {
            findNavController().navigateSafe(R.id.action_profileFragment_to_settingsFragment,
                bundleOf(USER to vm.user.value?.data))
        }

        binding.editProfileButton.setOnClickListener {
            findNavController().navigateSafe(R.id.action_profileFragment_to_editProfile,
                bundleOf(USER to vm.user.value?.data))
        }

        binding.buttonAddPhoto.setOnClickListener {
            ImagesBottomSheetDialog(requireContext(), imagesObserver).init()
        }

        return binding.root
    }

    private fun showProfile(user: ProfileModel) {
        // Load profile image using Picasso library
        Glide.with(requireContext())
            .load(user.pictureProfile.imageUrl)
            .apply(RequestOptions.overrideOf(640, 640))
            .centerCrop()
            .into(binding.profileImage)

        // Set about text if available, otherwise hide the view
        binding.about.text = user.about
        binding.about.visibility = if (user.about.isNotEmpty()) View.VISIBLE else View.GONE

        // Set profile name and age
        binding.profileName.text =
            resources.getString(R.string.name_and_age, user.username, user.birthday?.getAge())

        // Set verification visibility based on KYC status
        binding.verification.visibility = if (user.isVerification) View.VISIBLE else View.GONE

        // Set profile city
        binding.profileCity.text =
            resources.getString(R.string.city_and_country, user.city.city, user.city.country)

        // Set interests in interests recycler view
        profileInterestsAdapter.setInterests(user.interests)

        // Set photogallery images in photogallery recycler view
        profilePhotogalleryAdapter.setPhotogallery(user.pictures)
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

    @SuppressLint("ResourceType")
    private fun showGallery(position: Int, imageView: ImageView, overlay: OverlayPicturesView) {
        val gallery = vm.user.value?.data?.pictures

        CoroutineScope(Dispatchers.Main).launch {
            val viewerFragment = StfalconImageViewer.Builder(context, gallery) { view, picture ->
                Glide
                    .with(requireContext())
                    .load(picture.imageUrl)
                    .apply(RequestOptions.overrideOf(640, 640))
                    .centerCrop()
                    .into(view)
            }
                .withStartPosition(position)
                .withTransitionFrom(imageView)
                .withHiddenStatusBar(false)
                .withImageChangeListener { position ->
                    overlay.findViewById<TextView>(R.id.position_in_list).text =
                        "${position + 1} of ${gallery?.size}" ?: "exception"
                }

            viewerFragment.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycle.removeObserver(imagesObserver)
        _binding = null
    }
}