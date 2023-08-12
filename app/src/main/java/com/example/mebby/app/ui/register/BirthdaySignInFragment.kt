package com.example.mebby.app.ui.register

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.sealed.Birthday
import com.example.mebby.R
import com.example.mebby.app.customViews.birthday.BirthdayKeyListener
import com.example.mebby.app.customViews.birthday.BirthdayKeyListenerCallback
import com.example.mebby.app.customViews.birthday.BirthdayTextWatcher
import com.example.mebby.app.customViews.birthday.BirthdayTextWatcherCallbacks
import com.example.mebby.util.checkYears
import com.example.mebby.util.validateDate
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.databinding.FragmentBirthdaySignInBinding
import com.example.mebby.extensions.hideKeyboard


class BirthdaySignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentBirthdaySignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: RegistrationViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentBirthdaySignInBinding.inflate(inflater, container, false)


        binding.birthdayLinearLayout.birthday.observe(viewLifecycleOwner) { it ->
            Log.d("updateUser", it)
            val birthday = it.split("/")
            if (birthday.all { it.isDigitsOnly() }) {
                if (!validateDate(it)) {
                    Log.d("updateUser", "!validateDate(it)")
                } else if (!checkYears(it)) {
                    Log.d("updateUser", "!checkYears(it)")
                } else if (validateDate(it) && checkYears(it)) {
                    vm.setBirthday(it)
                    Log.d("updateUser", "validateDate(it) && checkYears(it)")
                }
            }
        }

        binding.buttonContinue.setOnClickListener {
            findNavController().navigate(R.id.action_birthdaySignInFragment_to_genderSignInFragment)
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        vm.birthday.observe(viewLifecycleOwner) { dateOfBirth ->
            val dateOfBirthList = dateOfBirth.split("/")

            if (dateOfBirthList.all { it.isDigitsOnly() }) {
                if (validateDate(dateOfBirth)) {
                    val checkYears = checkYears(dateOfBirth)

                    if (checkYears) {
                        binding.buttonContinue.isEnabled = true
                    } else {
                        binding.error.setText(R.string.error_18_age)
                        binding.error.visibility = View.VISIBLE

                        binding.buttonContinue.isEnabled = false
                    }

                } else {
                    binding.error.setText(R.string.error_invalid_date)
                    binding.error.visibility = View.VISIBLE

                    binding.buttonContinue.isEnabled = false
                }
            } else {
                binding.error.visibility = View.GONE

                binding.buttonContinue.isEnabled = false
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




