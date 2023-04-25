package com.example.mebby.app.ui.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mebby.R
import com.example.mebby.enums.FindTypes
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.databinding.FragmentFindSignInBinding


class FindSignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentFindSignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: RegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFindSignInBinding.inflate(inflater, container, false)

        var find = FindTypes.DATING

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.find_dating -> find = FindTypes.DATING
                R.id.find_friends -> find = FindTypes.FRIENDS
                R.id.find_partner -> find = FindTypes.PARTNER
            }
        }

        binding.buttonContinue.setOnClickListener {
            vm.setFindValue(find)
            findNavController().navigate(R.id.action_findSignInFragment_to_showSignInFragment)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}