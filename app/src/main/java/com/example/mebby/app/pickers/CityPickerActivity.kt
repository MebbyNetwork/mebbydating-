package com.example.mebby.app.pickers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.Resource
import com.example.domain.models.city.CityModel
import com.example.mebby.app.adapters.cityAdapter.ActionListener
import com.example.mebby.app.adapters.cityAdapter.CityRecyclerViewAdapter
import com.example.mebby.app.viewModels.CityPickerViewModel
import com.example.mebby.const.CITY_VALUE
import com.example.mebby.databinding.ActivityCityPickerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityPickerActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivityCityPickerBinding

    // ViewModel
    private lateinit var vm: CityPickerViewModel

    // RecyclerViewAdapter
    private val recyclerViewAdapter = CityRecyclerViewAdapter(object : ActionListener {
        override fun chooseCity(cityModel: CityModel) {
            finishActivityWithResult(cityModel)
        }
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[CityPickerViewModel::class.java]

        initRecyclerView()

        binding.back.setOnClickListener {
            Toast.makeText(this@CityPickerActivity, "Cancel", Toast.LENGTH_SHORT).show()
            finishActivity(RESULT_CANCELED)
            finishAndRemoveTask()
        }

        vm.cities.observe(this) { cities ->
            when (cities) {
                is Resource.Success -> {
                    cities.data?.let {
                        binding.progressBarRecyclerView.visibility = if (it.isNotEmpty()) View.GONE else View.VISIBLE
                        recyclerViewAdapter.setCitiesList(it)
                    }

                }
                is Resource.Error -> {

                }
                else -> {

                }
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.cityRecyclerView

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerViewAdapter
    }

    fun finishActivityWithResult(cityModel: CityModel) {
        val data = Intent()
        data.putExtra(CITY_VALUE, cityModel)
        setResult(RESULT_OK, data)
        finish()
    }
}