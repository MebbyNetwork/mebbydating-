package com.example.mebby.app.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecorator(
    var verticalSpaceHeight: Int = 0,
    var horizontalSpaceHeight: Int = 0,
    var spanCount: Int = 3,
    ) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if ((position - 1) % 2 == 0) {
            outRect.left = horizontalSpaceHeight
        }

        outRect.bottom = verticalSpaceHeight
    }
}