package com.example.mebby.app.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class InterestsItemDecorator(
    var verticalSpaceHeight: Int = 0,
    var horizontalSpaceHeight: Int = 0,
): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = verticalSpaceHeight
        outRect.bottom = horizontalSpaceHeight
    }
}