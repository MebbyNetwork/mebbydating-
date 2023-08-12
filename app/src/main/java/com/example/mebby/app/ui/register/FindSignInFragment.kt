package com.example.mebby.app.ui.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.sealed.Find
import com.example.mebby.R
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

        var find: Find = Find.Dating

        binding.back.setOnClickListener { findNavController().popBackStack() }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.find_dating -> find = Find.Dating
                R.id.find_friends -> find = Find.Friends
                R.id.find_partner -> find = Find.Partner
            }
        }

        binding.buttonContinue.setOnClickListener {
            vm.setFind(find)
            findNavController().navigate(R.id.action_findSignInFragment_to_showSignInFragment)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}