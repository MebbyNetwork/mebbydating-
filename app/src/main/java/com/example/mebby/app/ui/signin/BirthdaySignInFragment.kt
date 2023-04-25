package com.example.mebby.app.ui.signin

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mebby.R
import com.example.mebby.app.listeners.BirthdayKeyListener
import com.example.mebby.app.listeners.BirthdayKeyListenerCallback
import com.example.mebby.app.textWatchers.BirthdayTextWatcher
import com.example.mebby.app.textWatchers.BirthdayTextWatcherCallbacks
import com.example.mebby.util.checkYears
import com.example.mebby.util.validateDate
import com.example.mebby.enums.BirthdayValue
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

        val editTextDay = binding.editTextDay
        val editTextMonth = binding.editTextMonth
        val editTextYear = binding.editTextYear

        val editTextList = arrayListOf<EditText>(editTextDay, editTextMonth, editTextYear)

        val birthdayTextWatcherCallback = object : BirthdayTextWatcherCallbacks {
            override fun onTextChanged(value: String, type: BirthdayValue) {
                when (type) {
                    BirthdayValue.DAY -> {
                        if (value.length == 2) editTextMonth.requestFocus()
                        vm.setDay(value)
                    }
                    BirthdayValue.MONTH -> {
                        if (value.length == 2) editTextYear.requestFocus()
                        vm.setMonth(value)
                    }
                    BirthdayValue.YEAR -> {
                        if (value.length == 4) {
                            hideKeyboard()
                            binding.editTextYear.clearFocus()
                        }
                        vm.setYear(value)
                    }
                }
            }
        }

        val birthdayKeyListenersCallback = object : BirthdayKeyListenerCallback {
            override fun keyEventEnter(view: View) {
                view.clearFocus()
                hideKeyboard()
            }

            override fun keyEventDel(view: View) {
                val index = editTextList.indexOf(view)
                val editText = editTextList[index]

                if  (index > 0 && editText.text.toString().isEmpty()) {
                    editTextList[editTextList.indexOf(view) - 1].requestFocus()
                }
            }
        }

        editTextDay.addTextChangedListener(BirthdayTextWatcher(type = BirthdayValue.DAY, callbacks = birthdayTextWatcherCallback))

        editTextMonth.addTextChangedListener(BirthdayTextWatcher(type = BirthdayValue.MONTH, callbacks = birthdayTextWatcherCallback))

        editTextYear.addTextChangedListener(BirthdayTextWatcher(type = BirthdayValue.YEAR, callbacks = birthdayTextWatcherCallback))

        for (index in editTextList.indices) {
            editTextList[index].setOnKeyListener(BirthdayKeyListener(birthdayKeyListenersCallback))
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




