package com.example.mebby.app.ui.swipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mebby.databinding.FragmentSwipeMatchBinding

class SwipeMatchFragment : Fragment() {
    // View Binding
    private var _binding: FragmentSwipeMatchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSwipeMatchBinding.inflate(inflater, container, false)




        return binding.root
    }
}