package com.example.mebby.app.adapters.imageAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.models.PictureModel

class DiffUtilPhotoRecyclerView(
    private var oldImageModels: List<PictureModel?>,
    private var newImageModels: List<PictureModel?>
    ): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldImageModels.size
    override fun getNewListSize(): Int = newImageModels.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (oldImageModels[oldItemPosition] != null && newImageModels[newItemPosition] != null) {
            oldImageModels[oldItemPosition]?.imageUrl == newImageModels[newItemPosition]?.imageUrl
        } else false

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldImageModels[oldItemPosition] == newImageModels[newItemPosition]
    }
}
