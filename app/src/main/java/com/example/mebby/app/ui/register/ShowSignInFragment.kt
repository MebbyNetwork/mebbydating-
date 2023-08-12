package com.example.mebby.app.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.sealed.Gender
import com.example.domain.sealed.Show
import com.example.mebby.R
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.databinding.FragmentShowSignInBinding


class ShowSignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentShowSignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: RegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentShowSignInBinding.inflate(inflater, container, false)

        var show: Show = if (vm.gender.value == Gender.Male) Show.Women else Show.Men

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.show_men -> show = Show.Men
                R.id.show_women -> show = Show.Women
                R.id.show_all -> show = Show.Everyone
            }
        }

        binding.buttonContinue.setOnClickListener {
            vm.setShow(show)
            findNavController().navigate(R.id.action_showSignInFragment_to_photoSignInFragment)
        }

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}