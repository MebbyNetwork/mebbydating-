package com.example.mebby.app.adapters.cityAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mebby.domain.models.city.CityModel

class DiffUtilCityRecyclerView(
    private val oldList: List<CityModel>,
    private val newList: List<CityModel>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}