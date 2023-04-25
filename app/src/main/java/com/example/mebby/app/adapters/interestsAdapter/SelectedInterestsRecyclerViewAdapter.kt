package com.example.mebby.app.adapters.interestsAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mebby.databinding.SelectedInterestsLayoutBinding
import com.example.mebby.domain.models.InterestModel

interface SelectedInterestsRecyclerViewActionListener {
    fun deleteInterests(interestModel: InterestModel)
}

class SelectedInterestsRecyclerViewAdapter(
    private val actionListener: SelectedInterestsRecyclerViewActionListener
    ): RecyclerView.Adapter<SelectedInterestsRecyclerViewAdapter.ViewHolder>(), View.OnClickListener {

    class ViewHolder(val binding: SelectedInterestsLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private var selectedInterestsList: List<InterestModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SelectedInterestsLayoutBinding.inflate(inflater, parent, false)

        binding.selectedInterestLinearLayout.setOnClickListener(this)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val interest = selectedInterestsList[position]
        with(holder.binding) {
            holder.itemView.tag = interest
            selectedInterestLinearLayout.tag = interest

            textView.text = interest.value
        }
    }

    override fun getItemCount(): Int = selectedInterestsList.size

    override fun onClick(v: View) {
        val interestModel = v.tag as InterestModel
        actionListener.deleteInterests(interestModel)
    }

    fun setSelectedInterestsList(list: List<InterestModel>) {

        val diffCallback = DiffUtilInterestsRecyclerView(selectedInterestsList, list)

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        selectedInterestsList = list
        Log.d("MEBBYDATING", "ZAU = $list")
        diffResult.dispatchUpdatesTo(this)
    }
}