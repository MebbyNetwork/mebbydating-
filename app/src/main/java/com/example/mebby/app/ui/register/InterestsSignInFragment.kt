package com.example.mebby.app.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.InterestModel
import com.example.mebby.R
import com.example.mebby.app.adapters.interestsAdapter.InterestsActionListener
import com.example.mebby.app.adapters.interestsAdapter.InterestsRecyclerViewAdapter
import com.example.mebby.app.adapters.interestsAdapter.SelectedInterestActionListener
import com.example.mebby.app.adapters.interestsAdapter.SelectedInterestsRecyclerViewAdapter
import com.example.mebby.app.decorators.InterestsItemDecorator
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.databinding.FragmentInterestsSignInBinding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager


class InterestsSignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentInterestsSignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: RegistrationViewModel by activityViewModels()

    //InterestsRecyclerView
    private val interestsRecyclerViewAdapter =
        InterestsRecyclerViewAdapter(object : InterestsActionListener {
            override fun selectInterest(interest: InterestModel) {
                vm.selectInterest(interest)
            }

        })

    //SelectedInterestsRecyclerView
    private val selectedRecyclerViewAdapter =
        SelectedInterestsRecyclerViewAdapter(object : SelectedInterestActionListener {
            override fun deleteInterests(interest: InterestModel) {
                vm.unselectInterest(interest)
            }
        })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentInterestsSignInBinding.inflate(inflater, container, false)

        binding.editTextSearch.editText.addTextChangedListener(afterTextChanged = {
            filterInterestsList(it.toString())
        })

        initInterestsRecyclerView()
        initSelectedRecyclerView()

        vm.interests.observe(viewLifecycleOwner) {
            binding.progressBarRecyclerView.visibility =
                if (it.isNotEmpty()) View.GONE else View.VISIBLE

            //Search
            val searchText = binding.editTextSearch.editText.text.toString()
            if (searchText == "") interestsRecyclerViewAdapter.setInterestsList(it)
            else filterInterestsList(searchText)
        }

        vm.selectedInterest.observe(viewLifecycleOwner) {
            selectedRecyclerViewAdapter.setSelectedInterestsList(it)

            binding.selectedInterestsRecyclerView.visibility =
                if (it.isNotEmpty()) View.VISIBLE else View.GONE

            binding.buttonContinue.text =
                resources.getString(R.string.continue_text_with_counter, it.size, 5)

            binding.buttonContinue.isEnabled = it.isNotEmpty()
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonContinue.setOnClickListener {
            findNavController().navigate(R.id.action_interestsSignInFragment_to_aboutMeSignInFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initInterestsRecyclerView() {
        val recyclerView = binding.interestsRecyclerView
        val layoutManager = FlexboxLayoutManager(requireContext())

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = interestsRecyclerViewAdapter
        recyclerView.addItemDecoration(InterestsItemDecorator(12, 12))
    }

    private fun initSelectedRecyclerView() {
        val recyclerView = binding.selectedInterestsRecyclerView
        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = selectedRecyclerViewAdapter
        recyclerView.addItemDecoration(InterestsItemDecorator(12, 12))
    }

    private fun filterInterestsList(string: String) {
        val filteredList = ArrayList<InterestModel>()
        for (interest in vm.interests.value!!) {
            if (interest.interestValue.lowercase().startsWith(string.lowercase())) {
                filteredList.add(interest)
            }
        }
        filteredList.sortedBy { it.interestValue }
        interestsRecyclerViewAdapter.setInterestsList(filteredList)
    }
}



