package com.example.mebby.app.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mebby.R
import com.example.mebby.app.viewModels.SplashScreenViewModel
import com.example.mebby.data.Resource
import com.example.mebby.databinding.FragmentSplashScreenBinding
import com.example.mebby.enums.AuthResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
    // ViewBinding
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val vm by viewModels<SplashScreenViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        vm.checkAuth()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            vm.authLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        when (it.data) {
                            AuthResponse.UserIsRegistered -> {
                                findNavController().navigate(R.id.action_splashScreen_to_mainFragment)
                            }

                            AuthResponse.UserIsNotRegistered -> {
                                findNavController().navigate(R.id.action_splashScreen_to_registration_graph)
                            }

                            AuthResponse.UserIsNotLoggedIn -> {
                                findNavController().navigate(R.id.action_splashScreen_to_sign_in_graph)
                            }

                            else -> {}
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Loading -> {}
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
