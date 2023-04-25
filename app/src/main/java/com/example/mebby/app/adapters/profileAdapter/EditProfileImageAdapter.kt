package com.example.mebby.app.adapters.profileAdapter

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mebby.R
import com.example.mebby.app.adapters.imageAdapter.DiffUtilPhotoRecyclerView
import com.example.mebby.databinding.EditProfileImageLayoutBinding
import com.example.mebby.domain.models.ImageModel
import com.squareup.picasso.Picasso

interface ActionListener {
    fun addImage()

    fun selectImage(imageModel: ImageModel)
}

class EditProfileImageAdapter(
    private val actionListener: ActionListener,
    private val resources: Resources,
) : RecyclerView.Adapter<EditProfileImageAdapter.ViewHolder>(), View.OnClickListener {
    class ViewHolder(val binding: EditProfileImageLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    var images: List<ImageModel?> = mutableListOf(null)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EditProfileImageLayoutBinding.inflate(inflater, parent, false)

        binding.buttonAdd.setOnClickListener(this)
        binding.imageView.setOnClickListener(this)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]

        val screenWidth = holder.itemView.resources.displayMetrics.widthPixels
        val photoSize = screenWidth / 3

        holder.itemView.layoutParams.height = photoSize
        holder.itemView.layoutParams.width = photoSize

        with(holder.binding) {
            Log.d("ImagePosition", "${position}")
            Log.d("ImagePosition", "${image}")

            imageView.tag = image

            if (image != null) {
                if (image.generalImage) {
                    imageView.strokeColor = resources.getColorStateList(R.color.blue)
                    imageView.strokeWidth = resources.getDimension(R.dimen._8dp)
                } else {
                    imageView.strokeWidth = 0f
                }


                Picasso
                    .get()
                    .load(image.uri)
                    .resize(photoSize, photoSize)
                    .centerCrop()
                    .into(imageView)
                this.buttonAdd.visibility = View.GONE
            } else {
                Picasso
                    .get()
                    .load(R.color.white)
                    .resize(photoSize, photoSize)
                    .centerCrop()
                    .into(imageView)
                this.buttonAdd.visibility = View.VISIBLE

                imageView.strokeWidth = 0f
            }
        }
    }

    override fun getItemCount(): Int = images.size

    override fun onClick(v: View) {
        val image = v.tag as ImageModel?

        when (v.id) {
            R.id.button_add -> {
                actionListener.addImage()
            }

            R.id.image_view -> {
                Log.d("Checking", "$image")
                if (image != null) {
                    val newList = images.slice(1 until images.lastIndex + 1).toMutableList()

                    actionListener.selectImage(image)
                }

            }
        }
    }

    fun setImageList(list: List<ImageModel>) {
        Log.d("SetImageList", "${list.size}")

        val diffCallback = DiffUtilPhotoRecyclerView(images.slice(1 until images.lastIndex + 1), list)

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        images = listOf(null) + list

        diffResult.dispatchUpdatesTo(this)
    }
}