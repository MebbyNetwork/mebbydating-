package com.example.mebby.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mebby.R
import com.example.mebby.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    // ViewBinding
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val bottomNavigationView = binding.bottomNavigationView

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = navHostFragment!!.findNavController()

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, _, arguments ->
            bottomNavigationView.isVisible = arguments?.getBoolean("showAppBar", true) == true
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
