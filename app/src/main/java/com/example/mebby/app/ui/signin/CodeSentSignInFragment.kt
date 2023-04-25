package com.example.mebby.app.ui.signin

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mebby.R
import com.example.mebby.app.viewModels.PhoneAuthSignInViewModel
import com.example.mebby.data.Resource
import com.example.mebby.databinding.FragmentCodeSentSignInBinding
import com.example.mebby.enums.AuthResponse
import com.example.mebby.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CodeSentSignInFragment : Fragment() {
    private var _binding: FragmentCodeSentSignInBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private val vm: PhoneAuthSignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCodeSentSignInBinding.inflate(inflater, container, false)

        binding.resendCode.setOnClickListener {
            if (binding.resendCode.text == resources.getString(R.string.resend_code_complete)) {
                vm.resendVerificationCode(token = vm.getToken())
                resendCodeTimer(binding, resources)
            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.phoneNumber.text = resources.getString(R.string.number, vm.phoneNumber.value)

        vm.authLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    when (it.data) {
                        AuthResponse.UserIsRegistered -> {
                            findNavController().navigate(R.id.action_codeSentSignInFragment_to_mainFragment)
                        }

                        AuthResponse.UserIsNotRegistered -> {
                            findNavController().navigate(R.id.action_codeSentSignInFragment_to_registrationGraph)
                        }
                        else -> {}
                    }
                }

                is Resource.Loading -> {
                    Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        otpTextWatcher(binding, requireContext(), requireView()) {
            val otpCode = it.joinToString("") { otp -> otp.text.toString() }
            val verificationId = vm.getVerificationId()

            val credential = vm.getCredentialForPhoneAuth(verificationId, otpCode)

            vm.signInWithPhoneAuthCredential(credential)
        }
        resendCodeTimer(binding, resources)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun otpTextWatcher(
        binding: FragmentCodeSentSignInBinding,
        context: Context,
        view: View,
        callback: (list: List<EditText>) -> Unit
    ) {
        val firstNum = binding.otp1Number
        val secondNum = binding.otp2Number
        val thirdNum = binding.otp3Number
        val fourthNum = binding.otp4Number
        val fifthNum = binding.otp5Number
        val sixthNum = binding.otp6Number

        val otpEditText: List<EditText> = listOf(
            firstNum,
            secondNum,
            thirdNum,
            fourthNum,
            fifthNum,
            sixthNum
        )

        for (index in otpEditText.indices) {
            otpEditText[index].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val currentEditText = otpEditText[index]
                    val currentText = otpEditText[index].text.toString()

                    if (index == 5 && currentText.isNotEmpty()) {
                        currentEditText.clearFocus()
                        hideKeyboard()
                    } else if (currentText.isNotEmpty()) {
                        otpEditText[index + 1].requestFocus()
                    }

                    if (otpEditText.all { it.text.toString() != "" }) {
                        callback(otpEditText)
                    }
                }
            })

            otpEditText[index].setOnKeyListener(object : View.OnKeyListener {
                override fun onKey(v: View, keyCode: Int, event: KeyEvent?): Boolean {
                    val currentText = otpEditText[index].text.toString()

                    if (event?.action != KeyEvent.ACTION_DOWN) return false

                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        v.clearFocus()
                        hideKeyboard()
                    }

                    if (keyCode == KeyEvent.KEYCODE_DEL && currentText.isEmpty() && index > 0) {
                        otpEditText[index - 1].setText("")
                        otpEditText[index - 1].requestFocus()
                    }
                    return true
                }
            })
        }
    }

    private fun resendCodeTimer(binding: FragmentCodeSentSignInBinding, resources: Resources) {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendCode.text = resources.getString(
                    R.string.resend_code_wait,
                    (millisUntilFinished / 1000).toInt()
                )
            }

            override fun onFinish() {
                binding.resendCode.text = resources.getString(R.string.resend_code_complete)
            }
        }.start()
    }


}
