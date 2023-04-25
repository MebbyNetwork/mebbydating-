package com.example.mebby.app.adapters.profileAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mebby.R
import com.example.mebby.databinding.ProfileImageLayoutBinding
import com.example.mebby.domain.models.ImageModel

class ProfilePhotogalleryAdapter(
    private val metrics: Int = 0,
    private val context: android.content.Context,
) : RecyclerView.Adapter<ProfilePhotogalleryAdapter.ViewHolder>(), View.OnClickListener {
    class ViewHolder(val binding: ProfileImageLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private var images: List<ImageModel> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ProfileImageLayoutBinding.inflate(inflater, parent, false)

        binding.root.layoutParams.height = metrics
        binding.root.layoutParams.width = metrics

        binding.imageView.setOnClickListener(this)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]

        with(holder.binding) {
            holder.itemView.tag = image
            imageView.tag = image

            Glide
                .with(context)
                .load(image.uri)
                .apply(RequestOptions.overrideOf(250, 250))
                .centerCrop()
                .placeholder(R.color.shimmer)
                .into(imageView)
        }
    }

    override fun getItemCount(): Int = images.size

    override fun onClick(v: View) {
        val image = v.tag
        Log.d("image", "$image")
    }

    fun setPhotogallery(list: List<ImageModel>) {
        images = list
        Log.d("setPhotogallery", "$list")
        notifyDataSetChanged()
    }
}