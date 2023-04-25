package com.example.mebby.app.adapters.imageAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mebby.domain.models.ImageModel

class DiffUtilPhotoRecyclerView(
    private var oldImageModels: List<ImageModel?>,
    private var newImageModels: List<ImageModel?>
    ): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldImageModels.size
    override fun getNewListSize(): Int = newImageModels.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return if (oldImageModels[oldItemPosition] != null && newImageModels[newItemPosition] != null) {
            oldImageModels[oldItemPosition]?.uri == newImageModels[newItemPosition]?.uri &&
                    oldImageModels[oldItemPosition]?.generalImage == newImageModels[newItemPosition]?.generalImage
        } else false

    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldImageModels[oldItemPosition] == newImageModels[newItemPosition]
    }
}
