package com.example.mebby.app.adapters.profileAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.InterestModel
import com.example.mebby.databinding.ProfileInterestLayoutBinding

class ProfileInterestAdapter : RecyclerView.Adapter<ProfileInterestAdapter.ViewHolder>() {
    class ViewHolder(val binding: ProfileInterestLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private var interests: List<InterestModel> = mutableListOf()

    override fun getItemCount() = interests.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProfileInterestLayoutBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val interest = interests[position]

        with(holder.binding) {
            interestTextView.text = interest.interestValue
        }
    }

    fun setInterests(list: List<InterestModel>) {
        interests = list
        notifyDataSetChanged()
    }
}