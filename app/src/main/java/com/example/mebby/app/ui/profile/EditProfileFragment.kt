package com.example.mebby.app.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mebby.R
import com.example.mebby.app.adapters.interestsAdapter.InterestsRecyclerViewActionListener
import com.example.mebby.app.adapters.interestsAdapter.InterestsRecyclerViewAdapter
import com.example.mebby.app.adapters.interestsAdapter.SelectedInterestsRecyclerViewActionListener
import com.example.mebby.app.adapters.interestsAdapter.SelectedInterestsRecyclerViewAdapter
import com.example.mebby.app.adapters.profileAdapter.ActionListener
import com.example.mebby.app.adapters.profileAdapter.EditProfileImageAdapter
import com.example.mebby.app.decorators.EditProfileImageDecorator
import com.example.mebby.app.dialogs.ImagesBottomSheetDialog
import com.example.mebby.app.observers.ImagesObserver
import com.example.mebby.app.pickers.CityPickerActivity
import com.example.mebby.app.viewModels.EditProfileViewModel
import com.example.mebby.const.CITY_VALUE
import com.example.mebby.const.EDIT_PROFILE
import com.example.mebby.const.USER
import com.example.mebby.data.Resource
import com.example.mebby.databinding.FragmentEditProfileBinding
import com.example.mebby.domain.models.ImageModel
import com.example.mebby.domain.models.InterestModel
import com.example.mebby.domain.models.UserProfileModel
import com.example.mebby.domain.models.city.CityModel
import com.example.mebby.enums.GenderTypes
import com.example.mebby.util.checkYears
import com.example.mebby.util.convertTimestampToDate
import com.example.mebby.util.validateDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm by viewModels<EditProfileViewModel>()

    private var user: UserProfileModel? = null

    //EditProfileImageAdapter
    private val editProfileImageAdapter by lazy { EditProfileImageAdapter(object : ActionListener {
        override fun addImage() {
            ImagesBottomSheetDialog(requireContext(), imagesObserver).init()
        }

        override fun selectImage(imageModel: ImageModel) {
            vm.setGeneralImage(imageModel)
        }
    }, resources = resources) }

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    private lateinit var imagesObserver: ImagesObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val cityModel = data?.extras?.get(CITY_VALUE) as CityModel
                    vm.setLocation(cityModel)
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

        (activity as AppCompatActivity).setSupportActionBar(view?.findViewById(R.id.appbar))
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        //Get user
        user = arguments?.getParcelable(USER)
        Log.d("CHECKING", "$user")
        user?.let { vm.setUser(it) }

        //Initialize RecyclerViews()
        initGeneralPhotoRecyclerView()
        initInterestsView()


        with(vm.user.value) {
            binding.editTextName.setText(this?.username)

            this?.birthday?.let {
                val date = it.convertTimestampToDate()
                binding.birthdayLinearLayout.setBirthday(date)
            }

            binding.aboutEditText.setText(this?.about)
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            vm.changeUserProfile()
        }

        vm.changeState.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    setFragmentResult(EDIT_PROFILE, bundleOf(USER to true))

                    findNavController().popBackStack()
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                is Resource.Loading -> {
                    Toast.makeText(requireContext(), "Loading..", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // username
        usernameInitialize()

        // birthday
        birthdayInitialize()

        // about
        aboutInitialize()

        // location
        locationInitialize()

        // gender
        genderInitialize()
    }

    private fun genderInitialize() {
        binding.genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.gender_woman) vm.setGender(GenderTypes.WOMAN)
            if (checkedId == R.id.gender_man) vm.setGender(GenderTypes.MAN)
        }

        with(vm.user.value?.gender) {
            when (this) {
                GenderTypes.MAN.value -> {
                    binding.genderMan.isChecked = true
                }

                GenderTypes.WOMAN.value -> {
                    binding.genderWoman.isChecked = true
                }
            }
        }
    }

    private fun locationInitialize() {
        vm.user.observe(viewLifecycleOwner) {
            binding.locationTextView.text = "${it.city.city}, ${it.city.country}"

            binding.saveButton.visibility = if (it != user) View.VISIBLE else View.INVISIBLE
        }

        binding.locationPicker.setOnClickListener {
            startForResult.launch(Intent(requireContext(), CityPickerActivity::class.java))
        }
    }

    private fun aboutInitialize() {
        binding.aboutEditText.addTextChangedListener(afterTextChanged = {
            vm.setAbout(it.toString())
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun birthdayInitialize() {
        binding.birthdayLinearLayout.birthday.observe(viewLifecycleOwner) { it ->
            Log.d("updateUser", it)
            val birthday = it.split("/")
            if (birthday.all { it.isDigitsOnly() }) {
                if (!validateDate(it)) {
                    Log.d("updateUser", "!validateDate(it)")
                } else if (!checkYears(it)) {
                    Log.d("updateUser", "!checkYears(it)")
                } else if (validateDate(it) && checkYears(it)) {
                    vm.setBirthday(it)
                    Log.d("updateUser", "validateDate(it) && checkYears(it)")
                }
            }
        }
    }

    private fun usernameInitialize() {
        binding.editTextName.editText.addTextChangedListener(afterTextChanged = {
            vm.setName(it.toString())
        })
    }

    private fun initGeneralPhotoRecyclerView() {
        val recyclerView = binding.imageRecyclerView

        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        recyclerView.adapter = editProfileImageAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(EditProfileImageDecorator(resources.getDimensionPixelSize(R.dimen._8dp)))


        vm.user.observe(viewLifecycleOwner) { it ->

        }
    }

    private fun initInterestsView() {
        val interestsView = binding.interestsViewLayout

        // Selected Interests Adapter
        interestsView.setSelectedInterestsAdapter(
            SelectedInterestsRecyclerViewAdapter(object :
                SelectedInterestsRecyclerViewActionListener {
                override fun deleteInterests(interestModel: InterestModel) {
                    vm.removeInterestInSelectedListInterests(interestModel)
                }
            })
        )

        // Interests Adapter
        interestsView.setInterestsAdapter(
            InterestsRecyclerViewAdapter(object : InterestsRecyclerViewActionListener {
                override fun selectInterest(interestModel: InterestModel) {
                    vm.addInterestInSelectedListInterests(interestModel)
                }
            })
        )

        interestsView.search.editText.addTextChangedListener(afterTextChanged = {
            interestsView.filterInterestsList(it.toString(), vm.interestsList.value!!)
        })

        vm.user.observe(viewLifecycleOwner) {
            interestsView.setSelectedList(it.interests)
        }

        vm.interestsList.observe(viewLifecycleOwner) {
            val search = interestsView.search.editText
            if (search.text.toString() != "") interestsView.filterInterestsList(search.text.toString(),
                it)
            interestsView.setInterestsList(it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}