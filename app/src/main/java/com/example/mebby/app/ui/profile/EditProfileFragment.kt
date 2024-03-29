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
import com.example.domain.Resource
import com.example.domain.models.InterestModel
import com.example.domain.models.PictureModel
import com.example.domain.models.ProfileModel
import com.example.domain.models.city.CityModel
import com.example.domain.sealed.Gender
import com.example.mebby.R
import com.example.mebby.app.adapters.interestsAdapter.InterestsActionListener
import com.example.mebby.app.adapters.interestsAdapter.InterestsRecyclerViewAdapter
import com.example.mebby.app.adapters.interestsAdapter.SelectedInterestActionListener
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
import com.example.mebby.databinding.FragmentEditProfileBinding
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

    private var user: ProfileModel? = null

    //EditProfileImageAdapter
    private val editProfileImageAdapter by lazy { EditProfileImageAdapter(object : ActionListener {
        override fun addPicture() {
            ImagesBottomSheetDialog(requireContext(), imagesObserver).init()
        }

        override fun selectPicture(picture: PictureModel) {
            vm.setGeneralImage(picture)
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
            actionListener = object : com.example.mebby.app.observers.ActionListener() {
                override fun addPicture(picture: PictureModel) {
                    vm.addImage(picture)
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
        user = arguments?.getSerializable(USER) as? ProfileModel
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
                    Log.d("changeState", "${it.exception}")
                    Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_SHORT).show()
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
            if (checkedId == R.id.gender_woman) vm.setGender(Gender.Male)
            if (checkedId == R.id.gender_man) vm.setGender(Gender.Female)
        }

        with(vm.user.value?.genderType) {
            when (this) {
                Gender.Male.value -> {
                    binding.genderMan.isChecked = true
                }

                Gender.Female.value -> {
                    binding.genderWoman.isChecked = true
                }
            }
        }
    }

    private fun locationInitialize() {
        vm.user.observe(viewLifecycleOwner) {
            binding.locationTextView.text = resources.getString(R.string.city_and_country, it.city.city, it.city.country)

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

    }

    private fun initInterestsView() {
        val interestsView = binding.interestsViewLayout

        val selectedAdapter = SelectedInterestsRecyclerViewAdapter(object : SelectedInterestActionListener {
            override fun deleteInterests(interest: InterestModel) {
                vm.unselectInterest(interest)
            }
        })

        val interestAdapter = InterestsRecyclerViewAdapter(object : InterestsActionListener {
            override fun selectInterest(interest: InterestModel) {
                vm.selectInterest(interest)
            }
        })

        interestsView
            .setInterestsAdapter(interestAdapter)
            .setSelectedInterestsAdapter(selectedAdapter)
            .setSearchChange { vm.interestsList.value?.let { interests -> interestsView.filterInterestsList(it, interests) } }

        vm.user.observe(viewLifecycleOwner) { interestsView.setSelectedList(it.interests) }

        vm.interestsList.observe(viewLifecycleOwner) {
            val search = interestsView.search.editText.text.toString()
            if (search != "") interestsView.filterInterestsList(search, it)
            interestsView.setInterestsList(it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}