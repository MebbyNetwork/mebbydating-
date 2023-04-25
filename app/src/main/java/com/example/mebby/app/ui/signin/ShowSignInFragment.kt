package com.example.mebby.app.ui.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mebby.R
import com.example.mebby.enums.ShowTypes
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

        var show = ShowTypes.MEN

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.show_men -> show = ShowTypes.MEN
                R.id.show_women -> show = ShowTypes.WOMEN
                R.id.show_all -> show = ShowTypes.ALL
            }
        }

        binding.buttonContinue.setOnClickListener {
            vm.setShowValue(show)
            findNavController().navigate(R.id.action_showSignInFragment_to_photoSignInFragment)
        }

        return binding.root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}