package com.example.mebby.app.ui.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mebby.R
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.databinding.FragmentAboutMeSignInBinding


class AboutMeSignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentAboutMeSignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: RegistrationViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAboutMeSignInBinding.inflate(inflater, container, false)

        binding.counter.text = resources.getString(R.string._1_d_300, 0)

        binding.aboutMeEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.counter.text = resources.getString(R.string._1_d_300, s?.length ?: 0)
                vm.setAbout(s.toString())
            }
        })

        binding.skip.setOnClickListener {
            vm.setAbout("")
            findNavController().navigate(R.id.action_aboutMeSignInFragment_to_citySignInFragment)
        }

        binding.buttonContinue.setOnClickListener {
            if (binding.aboutMeEditText.text.toString() == "") vm.setAbout("")
            findNavController().navigate(R.id.action_aboutMeSignInFragment_to_citySignInFragment)
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}