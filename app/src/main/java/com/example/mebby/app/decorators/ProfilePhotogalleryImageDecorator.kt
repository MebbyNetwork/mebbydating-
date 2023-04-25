package com.example.mebby.app.decorators

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.marginTop
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.DoubleBuffer
import kotlin.math.roundToInt

class ProfilePhotogalleryImageDecorator(
    var verticalSpaceHeight: Int = 12,
    var horizontalSpaceHeight: Int = 12,
    var spanCount: Int = 3,
    ) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val columns = BigDecimal(parent.adapter?.itemCount!!.toDouble() / spanCount).setScale(0, RoundingMode.CEILING).toInt()

        val lastColumn = BigDecimal((position + 1).toDouble() / columns).setScale(0, RoundingMode.CEILING).toInt() != columns

        if (position == 1 || (position  - 1) % spanCount == 0) {
            Log.d("position", "1 = $lastColumn")
            view.setPadding((verticalSpaceHeight * 0.33).toInt(), 0, (verticalSpaceHeight * 0.33).toInt(), if (true) horizontalSpaceHeight else 0)
        }

        if (position % 3 == 0 || position == 0) {
            Log.d("position", "2 = $lastColumn")
            view.setPadding(0, 0, (verticalSpaceHeight * 0.66).roundToInt(), if (true) horizontalSpaceHeight else 0)
        }

        if ((position == 2 || (position - 2) % spanCount == 0) && position >= 2) {
            Log.d("position", "3 = $lastColumn")
            view.setPadding((verticalSpaceHeight * 0.66).roundToInt(), 0, 0, if (true) horizontalSpaceHeight else 0)
        }
    }
}