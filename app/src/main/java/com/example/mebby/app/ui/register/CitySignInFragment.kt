package com.example.mebby.app.ui.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.domain.models.city.CityModel
import com.example.mebby.R
import com.example.mebby.app.pickers.CityPickerActivity
import com.example.mebby.app.viewModels.RegistrationViewModel
import com.example.mebby.const.CITY_VALUE
import com.example.mebby.databinding.FragmentCitySignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitySignInFragment : Fragment() {
    //ViewBinding
    private var _binding: FragmentCitySignInBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val vm by activityViewModels<RegistrationViewModel>()

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val cityModel = data?.extras?.get(CITY_VALUE) as CityModel
                    vm.setCity(cityModel)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCitySignInBinding.inflate(inflater, container, false)

        val selectCity = binding.selectCity

        selectCity.setOnClickListener {
            startForResult.launch(Intent(requireContext(), CityPickerActivity::class.java))
        }

        vm.city.observe(viewLifecycleOwner) {
            binding.cityTextView.text = resources.getString(R.string.city_and_country, it.city, it.country)

            binding.buttonContinue.isEnabled = true
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonContinue.setOnClickListener {
            findNavController().navigate(R.id.action_citySignInFragment_to_createUserProfile)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}