package com.example.mebby.app.adapters.interestsAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mebby.domain.models.InterestModel

class DiffUtilInterestsRecyclerView(
    private val oldList: List<InterestModel>,
    private val newList: List<InterestModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].key == newList[newItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}