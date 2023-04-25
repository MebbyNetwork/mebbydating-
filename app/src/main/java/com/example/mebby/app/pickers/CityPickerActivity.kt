package com.example.mebby.app.pickers
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mebby.app.adapters.CityRecyclerViewActionListener
import com.example.mebby.app.adapters.CityRecyclerViewAdapter
import com.example.mebby.app.viewModels.CityPickerViewModel
import com.example.mebby.const.CITY_VALUE
import com.example.mebby.databinding.ActivityCityPickerBinding
import com.example.mebby.domain.models.city.CityModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityPickerActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivityCityPickerBinding

    // ViewModel
    private val vm: CityPickerViewModel by viewModels()

    // RecyclerViewAdapter
    private val recyclerViewAdapter = CityRecyclerViewAdapter(object : CityRecyclerViewActionListener {
        override fun chooseCity(cityModel: CityModel) {
            finishActivityWithResult(cityModel)
        }
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCityPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()

        binding.back.setOnClickListener {
            Toast.makeText(this@CityPickerActivity, "ZERG", Toast.LENGTH_SHORT).show()
            finishActivity(RESULT_CANCELED)
            finishAndRemoveTask()
        }

        vm.cityModelList.observe(this) { cities ->
            binding.progressBarRecyclerView.visibility = if (cities.isNotEmpty()) View.GONE else View.VISIBLE
            recyclerViewAdapter.setCitiesList(cities)
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