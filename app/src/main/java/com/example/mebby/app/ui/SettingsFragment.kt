package com.example.mebby.app.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.createGraph
import androidx.navigation.fragment.findNavController
import com.example.mebby.R
import com.example.mebby.app.viewModels.SettingsViewModel
import com.example.mebby.data.Resource
import com.example.mebby.databinding.FragmentSettingsBinding
import com.example.mebby.extensions.navigateSafe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm by activityViewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)


        binding.logOut.setOnClickListener {
            vm.signOutUseCase()
        }

        vm.auth.observe(viewLifecycleOwner) {
            Log.d("SettingsFragment", "${it?.data}")
            when (it) {
                is Resource.Success -> {
                    if (it.data!!) {
                        Log.d("SettingsFragment", "success")
                        findNavController().clearBackStack(R.id.settingsFragment)
                        findNavController().setGraph(R.navigation.nav_graph)
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
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