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
import com.example.domain.Resource
import com.example.domain.sealed.AuthStates
import com.example.mebby.R
import com.example.mebby.app.viewModels.SplashScreenViewModel
import com.example.mebby.databinding.FragmentSplashScreenBinding
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
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater, container, false)


        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            vm.authLiveData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Success -> {
                        when (it.data) {
                            AuthStates.IsRegistered -> {
                                findNavController().navigate(R.id.action_splashScreen_to_mainFragment)
                            }

                            AuthStates.IsNotRegistered -> {
                                findNavController().navigate(R.id.action_splashScreen_to_registration_graph)
                            }

                            AuthStates.IsNotLogged -> {
                                findNavController().navigate(R.id.action_splashScreen_to_sign_in_graph)
                            }

                            else -> {}
                        }
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_SHORT).show()
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
