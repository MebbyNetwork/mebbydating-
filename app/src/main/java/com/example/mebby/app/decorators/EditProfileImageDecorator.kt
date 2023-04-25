package com.example.mebby.app.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class EditProfileImageDecorator(private val itemOffset: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)


        if (position == 0) {
            outRect.left = itemOffset * 2
        }

        outRect.right = itemOffset


    }
}