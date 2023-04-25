package com.example.mebby.app.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperActionListener {
    fun move(sourcePosition: Int, targetPosition: Int): Unit
}

class ItemTouchHelperCallbacks(private val itemTouchHelperActionListener: ItemTouchHelperActionListener): ItemTouchHelper.SimpleCallback(
    (ItemTouchHelper.UP or ItemTouchHelper.END or ItemTouchHelper.START or ItemTouchHelper.DOWN), 0) {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        val sourcePosition = viewHolder.adapterPosition
        val targetPosition = target.adapterPosition

        if (target.itemView.tag == null || viewHolder.itemView.tag == null) return false

        itemTouchHelperActionListener.move(sourcePosition, targetPosition)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            viewHolder?.itemView?.scaleX = 0.98f
            viewHolder?.itemView?.scaleY = 0.98f
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.scaleX = 1f
        viewHolder.itemView.scaleY = 1f
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        var dragFlags = ItemTouchHelper.UP or ItemTouchHelper.END or ItemTouchHelper.START or ItemTouchHelper.DOWN
        val swipeFlags = 0
        if (viewHolder.itemView.tag == null) dragFlags = 0
        return makeMovementFlags(dragFlags, swipeFlags)

    }

}