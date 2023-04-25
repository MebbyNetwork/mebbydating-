package com.example.mebby.app.adapters.imageAdapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mebby.R
import com.example.mebby.databinding.AddPhotoLayoutBinding
import com.example.mebby.domain.models.ImageModel

interface AddPhotoActionListener {
    fun addPhoto()
    fun deletePhoto(imageModel: ImageModel)
}

class PhotoRecyclerViewAdapter(
    private val actionListener: AddPhotoActionListener
    ) : RecyclerView.Adapter<PhotoRecyclerViewAdapter.PhotoViewHolder>(), View.OnClickListener {

    class PhotoViewHolder(val binding: AddPhotoLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private var imageModels: MutableList<ImageModel?> = mutableListOf(null)

    private val limit = 6

    override fun getItemCount(): Int = if (imageModels.size > limit) limit else imageModels.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AddPhotoLayoutBinding.inflate(inflater, parent, false)

        binding.buttonAdd.setOnClickListener(this)
        binding.buttonDelete.setOnClickListener(this)

        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = imageModels[position]
        with(holder.binding) {
            holder.itemView.tag = photo
            buttonAdd.tag = photo
            buttonDelete.tag = photo
            if (photo != null) {
                Log.d("photoUri", photo.uri)
                Glide.with(holder.itemView).load(photo.uri).centerCrop().into(imageView)
                buttonDelete.visibility = View.VISIBLE
                buttonAdd.visibility = View.GONE
            }
            else {
                Glide.with(holder.itemView).load(R.color.white).centerCrop().into(imageView)
                buttonDelete.visibility = View.GONE
                buttonAdd.visibility = View.VISIBLE
            }

        }
    }

    override fun onClick(v: View) {
        val imageModel = v.tag as ImageModel?
        when (v.id) {
            R.id.button_add -> {
                actionListener.addPhoto()
            }

            R.id.button_delete -> {
                if (imageModel != null) {
                    actionListener.deletePhoto(imageModel)
                }
            }
        }
    }

    fun setList(list: List<ImageModel?>) {
        val diffCallback: DiffUtilPhotoRecyclerView = if (list.size == 6) {
            DiffUtilPhotoRecyclerView(imageModels, list)
        } else {
            DiffUtilPhotoRecyclerView(imageModels.slice(0 until imageModels.lastIndex), list)
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)

        imageModels = list.plus(null).toMutableList()

        diffResult.dispatchUpdatesTo(this)
    }
}