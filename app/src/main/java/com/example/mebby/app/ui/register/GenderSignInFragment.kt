package com.example.mebby.app.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.sealed.Gender
import com.example.mebby.R
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.databinding.FragmentGenderSignInBinding


class GenderSignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentGenderSignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGenderSignInBinding.inflate(inflater, container, false)

        binding.back.setOnClickListener { findNavController().popBackStack() }

        var gender: Gender = Gender.Male

        binding.buttonContinue.setOnClickListener {
            vm.setGender(gender)

            findNavController().navigate(R.id.action_genderSignInFragment_to_findSignInFragment)
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.gender_woman) gender = Gender.Male
            if (checkedId == R.id.gender_man) gender = Gender.Female
        }

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
