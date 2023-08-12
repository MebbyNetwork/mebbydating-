package com.example.mebby.app.ui.swipes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.Resource
import com.example.domain.models.InterestModel
import com.example.domain.models.city.CityModel
import com.example.domain.models.swipes.SwipesFiltersModel
import com.example.domain.sealed.Find
import com.example.domain.sealed.Show
import com.example.mebby.R
import com.example.mebby.app.adapters.interestsAdapter.InterestsActionListener
import com.example.mebby.app.adapters.interestsAdapter.InterestsRecyclerViewAdapter
import com.example.mebby.app.adapters.interestsAdapter.SelectedInterestActionListener
import com.example.mebby.app.adapters.interestsAdapter.SelectedInterestsRecyclerViewAdapter
import com.example.mebby.app.viewModels.SwipeFilterViewModel
import com.example.mebby.const.CITY_VALUE
import com.example.mebby.const.EDIT_PROFILE
import com.example.mebby.const.USER
import com.example.mebby.databinding.FragmentSwipeFilterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SwipeFilterFragment : Fragment() {
    // ViewBiding
    private var _binding: FragmentSwipeFilterBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val vm by viewModels<SwipeFilterViewModel>()

    // City Picker
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    // On Create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val location = data?.extras?.get(CITY_VALUE) as CityModel
                    vm.changeLocation(location)
                }
            }
    }

    // On Create View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSwipeFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener { vm.updateFilters() }

        vm.changeState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    setFragmentResult(EDIT_PROFILE, bundleOf("isSwipesFilterChanged" to true))

                    findNavController().popBackStack()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        vm.getSwipeFilters {
            when (it) {
                is Resource.Success -> {
                    initFilters(it.data!!)

                    binding.nestedScrollView.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.nestedScrollView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Error -> {

                }
            }
        }

        vm.filters.observe(viewLifecycleOwner) {
            Log.d("filtersChanged", "${it}")
        }
    }

    private fun initFilters(it: SwipesFiltersModel) {
        Log.d("initFilters", "$it")
        vm.initFilters(it)
        initializeInitialValues()
        initializeListeners()
        initInterestRecyclerView()
    }

    private fun initializeInitialValues() {
        // Initialize the initial AgeRange value
        Log.d("initFilters", "${vm.filters.value?.ageRange}")
        with(vm.filters.value?.ageRange) {
            binding.counterAgeRange.text = "${this?.startAge} - ${this?.endAge}"

            binding.ageRangeSeekBar.values = listOf(
                this?.startAge?.toFloat(),
                this?.endAge?.toFloat()
            )
        }

        // Initialize the initial Location value
        with(vm.filters.value?.location) {
            binding.locationTextView.text = resources.getString(R.string.city_and_country, this?.city, this?.country)
        }

        // Initialize the initial Show value
        with(vm.filters.value?.show) {
            when (this) {
                Show.Everyone.value -> binding.genderAll.isChecked = true
                Show.Men.value -> binding.genderMan.isChecked = true
                Show.Women.value -> binding.genderWoman.isChecked = true
            }
        }

        // Initialize the initial Find value
        with(vm.filters.value?.find) {
            when (this) {
                Find.Friends.value -> binding.findFriends.isChecked = true
                Find.Partner.value -> binding.findPartner.isChecked = true
                Find.Dating.value -> binding.findDating.isChecked = true
            }
        }
    }

    private fun initializeListeners() {
        // Change changeListener for ageRange value
        binding.ageRangeSeekBar.addOnChangeListener { slider, _, _ ->
            binding.counterAgeRange.text = "${slider.values[0].toInt()} - ${slider.values[1].toInt()}"
            vm.changeAgeRange(slider.values)
        }

        // Set changeListener for Show value
        binding.interestGenderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.gender_man -> vm.changeShow(Show.Men)
                R.id.gender_woman -> vm.changeShow(Show.Women)
                R.id.gender_all -> vm.changeShow(Show.Everyone)
            }

        }

        // Set changeListener for Find value
        binding.findRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.find_friends -> vm.changeFind(Find.Friends)
                R.id.find_partner -> vm.changeFind(Find.Partner)
                R.id.find_dating -> vm.changeFind(Find.Dating)
            }
        }
    }

    private fun initInterestRecyclerView() {
        val interestView = binding.interestsViewLayout

        vm.initInterests()

        val selectedAdapter = SelectedInterestsRecyclerViewAdapter(object : SelectedInterestActionListener {
            override fun deleteInterests(interest: InterestModel) {
                Log.d("initInterestRecyclerView", "deleteInterests")
                vm.unselectInterest(interest)
            }
        })

        val interestAdapter = InterestsRecyclerViewAdapter(object : InterestsActionListener {
            override fun selectInterest(interest: InterestModel) {
                Log.d("initInterestRecyclerView", "selectInterest")
                vm.selectInterest(interest)
            }
        })

        interestView
            .setInterestsAdapter(interestAdapter)
            .setSelectedInterestsAdapter(selectedAdapter)
            .setSearchChange { vm.interests.value?.let { interests -> interestView.filterInterestsList(it, interests) } }

        vm.filters.observe(viewLifecycleOwner) { filter ->
            interestView.setSelectedList(filter.interest ?: listOf())
            Log.d("initInterestRecyclerView", "filter observer = ${filter.interest}")
        }

        vm.interests.observe(viewLifecycleOwner) {
            val search = interestView.search.editText.text.toString()
            if (search != "") interestView.filterInterestsList(search, it)
            interestView.setInterestsList(it)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}