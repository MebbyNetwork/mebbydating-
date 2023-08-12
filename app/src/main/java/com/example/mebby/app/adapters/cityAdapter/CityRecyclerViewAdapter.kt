package com.example.mebby.app.adapters.cityAdapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.city.CityModel
import com.example.mebby.R
import com.example.mebby.databinding.CityItemLayoutBinding

interface ActionListener {
    fun chooseCity(cityModel: CityModel)
}

class CityRecyclerViewAdapter(
    private val actionListener: ActionListener
): RecyclerView.Adapter<CityRecyclerViewAdapter.ViewHolder>(), View.OnClickListener {
    class ViewHolder(val binding: CityItemLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private var citiesList: List<CityModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = CityItemLayoutBinding.inflate(inflater, parent, false)

        binding.cityItem.setOnClickListener(this)

        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = citiesList[position]

        with(holder.binding) {
            holder.itemView.tag = city
            cityName.tag = city

            cityName.text = "${city.name}, ${city.country}"

        }
    }

    override fun getItemCount(): Int = citiesList.size

    override fun onClick(v: View) {
        val cityModel = v.tag as CityModel
        when (v.id) {
            R.id.city_item -> {
                actionListener.chooseCity(cityModel)
            }
        }
    }

    fun setCitiesList(list: List<CityModel>) {
        val diffCallback = DiffUtilCityRecyclerView(citiesList, list)

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        citiesList = list

        diffResult.dispatchUpdatesTo(this)
    }
}