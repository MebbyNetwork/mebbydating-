package com.example.mebby.app.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.Resource
import com.example.mebby.R
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.databinding.FragmentCreateUserProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateUserProfile : Fragment() {
    // ViewBinding
    private var _binding: FragmentCreateUserProfileBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val vm by activityViewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateUserProfileBinding.inflate(inflater, container, false)

        vm.createProfile()

        vm.authLiveData.observe(viewLifecycleOwner) {
            Log.d("authFlow", "$it")

            when (it) {
                is Resource.Success -> {
                    Toast.makeText(requireContext(), it.data.toString(), Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_createUserProfile_to_mainFragment)
                }

                is Resource.Error -> {
                    Log.d("Resource.Error", "${it.exception?.message}")
                }

                is Resource.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
