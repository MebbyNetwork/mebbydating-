package com.example.mebby.app.adapters.profileAdapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.domain.models.PictureModel
import com.example.mebby.R
import com.example.mebby.app.customViews.OverlayPicturesView
import com.example.mebby.databinding.ProfileImageLayoutBinding
import com.squareup.picasso.Picasso
import com.stfalcon.imageviewer.StfalconImageViewer
import com.stfalcon.imageviewer.listeners.OnImageChangeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.wait

interface PhotogalleryActionListener {
    fun showPicture(position: Int, imageView: ImageView)
}

class ProfilePhotogalleryAdapter(
    private val metrics: Int = 0,
    private val context: android.content.Context,
    private val photogalleryActionListener: PhotogalleryActionListener
) : RecyclerView.Adapter<ProfilePhotogalleryAdapter.ViewHolder>(), View.OnClickListener {
    class ViewHolder(val binding: ProfileImageLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private var images: List<PictureModel> = mutableListOf()


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
                .load(image.imageUrl)
                .apply(RequestOptions.overrideOf(250, 250))
                .centerCrop()
                .placeholder(R.color.shimmer)
                .into(imageView)
        }
    }

    override fun getItemCount(): Int = images.size

    override fun onClick(v: View) {
        val image = v.tag as PictureModel
        Log.d("image", "$image")
        when (v.id) {
            R.id.image_view -> {
                val position = images.indexOf(image)
                photogalleryActionListener.showPicture(position, v as ImageView)
            }
        }
    }

    fun setPhotogallery(list: List<PictureModel>) {
        images = list
        Log.d("setPhotogallery", "$list")
        notifyDataSetChanged()
    }
}