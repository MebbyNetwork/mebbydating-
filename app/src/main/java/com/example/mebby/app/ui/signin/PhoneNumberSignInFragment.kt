package com.example.mebby.app.ui.signin

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.InputType
import android.text.method.LinkMovementMethod
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.mebby.R
import com.example.mebby.app.viewModels.PhoneAuthSignInViewModel
import com.example.mebby.databinding.FragmentPhoneNumberSignInBinding
import com.example.mebby.domain.states.PhoneAuthState
import com.example.mebby.extensions.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneNumberSignInFragment : Fragment() {
    //Binding
    private var _binding: FragmentPhoneNumberSignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm: PhoneAuthSignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPhoneNumberSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editTextPhoneNumber = binding.editTextPhoneNumber
        editTextPhoneNumber.inputType = InputType.TYPE_CLASS_PHONE

        editTextPhoneNumber.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        editTextPhoneNumber.addTextChangedListener {
            vm.changePhoneNumber(binding.editTextPhoneNumber.text.toString())
        }

        binding.agreement.linksClickable = true
        binding.agreement.movementMethod = LinkMovementMethod.getInstance()

        binding.buttonContinue.setOnClickListener {
            vm.sendVerificationCode()
            binding.editTextPhoneNumber.clearFocus()
            hideKeyboard()
        }

        vm.mutableStateSendCode.observe(viewLifecycleOwner) {
            if (it == PhoneAuthState.SUCCESS) {
                findNavController().navigate(R.id.action_phoneNumberSignInFragment_to_codeSentSignInFragment)
                vm.mutableStateSendCode.value = PhoneAuthState.DEFAULT
            }
        }

        vm.phoneNumber.observe(viewLifecycleOwner) {
            binding.buttonContinue.isEnabled = vm.deleteExtraSymbols().length > 9
        }

        binding.editTextPhoneNumber.setOnKeyListener { v, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                v.clearFocus()
                hideKeyboard()
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}