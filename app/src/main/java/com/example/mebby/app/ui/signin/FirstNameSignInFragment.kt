package com.example.mebby.app.ui.signin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mebby.R
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.databinding.FragmentFirstNameSignInBinding
import com.example.mebby.extensions.hideKeyboard


class FirstNameSignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentFirstNameSignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: RegistrationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFirstNameSignInBinding.inflate(inflater, container, false)

        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearText.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE

            }

            override fun afterTextChanged(s: Editable?) {
                vm.setFirstName(s.toString())
            }

        })

        binding.clearText.setOnClickListener {
            binding.editTextName.text?.clear()
        }

        vm.name.observe(viewLifecycleOwner) {
            binding.buttonContinue.isEnabled = it.length > 2
        }

        binding.editTextName.setOnKeyListener { v, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                v.clearFocus()
                hideKeyboard()
            }
            true
        }

        binding.buttonContinue.setOnClickListener {
            findNavController().navigate(R.id.action_firstNameSignInFragment_to_birthdaySignInFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}