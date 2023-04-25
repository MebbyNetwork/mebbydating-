package com.example.mebby.app.adapters.interestsAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mebby.R
import com.example.mebby.databinding.InterestLayoutBinding
import com.example.mebby.domain.models.InterestModel

interface InterestsRecyclerViewActionListener {
    fun selectInterest(interestModel: InterestModel)
}

class InterestsRecyclerViewAdapter(
        private val actionListener: InterestsRecyclerViewActionListener
    ) : RecyclerView.Adapter<InterestsRecyclerViewAdapter.ViewHolder>(), View.OnClickListener {
    class ViewHolder(val binding: InterestLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private var interestsList: List<InterestModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = InterestLayoutBinding.inflate(inflater, parent, false)

        binding.interestTextView.setOnClickListener(this)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val interest = interestsList[position]

        with(holder.binding) {
            holder.itemView.tag = interest
            interestTextView.tag = interest

            interestTextView.text = interest.value
        }
    }

    override fun getItemCount(): Int = interestsList.size

    override fun onClick(v: View) {
        val interestModel = v.tag as InterestModel
        Log.d("MEBBYDATING", "INTEREST = $interestModel")
        when (v.id) {
            R.id.interest_text_view -> { actionListener.selectInterest(interestModel) }
        }
    }

    fun setInterestsList(list: List<InterestModel>) {
        val diffCallback = DiffUtilInterestsRecyclerView(interestsList, list)

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        interestsList = list

        diffResult.dispatchUpdatesTo(this)
    }

}