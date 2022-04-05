package com.bguilherme.financialmanager.view.bill.adapter

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bguilherme.financialmanager.FinancialManagerApplication
import com.bguilherme.financialmanager.R

// https://medium.com/@zackcosborn/step-by-step-recyclerview-swipe-to-delete-and-undo-7bbae1fce27e
// https://medium.com/@kitek/recyclerview-swipe-to-delete-easier-than-you-thought-cff67ff5e5f6
// https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf

class RecyclerViewSwipeCallBack(private val mAdapter: BillAdapter) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private var icon: Drawable? = null
    private var background: ColorDrawable? = null

    init {
        icon = AppCompatResources.getDrawable(
            FinancialManagerApplication.getInstance()?.applicationContext!!,
            R.drawable.ic_delete
        )
        background = ColorDrawable(Color.RED)
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if (viewHolder is BillAdapter.HeaderFooterViewHolder) {
            return
        }

        val itemView = viewHolder.itemView

        val iconMargin: Int = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - icon!!.intrinsicHeight) / 2
        val iconBottom = iconTop + icon!!.intrinsicHeight

        if (dX > 0) { // Swiping to the right
            val iconRight: Int = itemView.left + iconMargin + icon!!.intrinsicWidth
            val iconLeft: Int = itemView.left + iconMargin
            icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background!!.setBounds(
                itemView.left, itemView.top,
                itemView.left + dX.toInt(), itemView.bottom
            )
        } else if (dX < 0) { // Swiping to the left
            val iconLeft: Int = itemView.right - iconMargin - icon!!.intrinsicWidth
            val iconRight: Int = itemView.right - iconMargin
            icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background!!.setBounds(
                itemView.right + dX.toInt(),
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            background!!.setBounds(0, 0, 0, 0)
        }

        background!!.draw(c)
        icon!!.draw(c)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        if (viewHolder is BillAdapter.HeaderFooterViewHolder) {
            return false
        }

        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        mAdapter.notifyItemMoved(fromPosition, toPosition)
        mAdapter.onMoved(fromPosition, toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        if (viewHolder is BillAdapter.HeaderFooterViewHolder) {
            return
        }

        val position = viewHolder.adapterPosition
        mAdapter.removeItemAt(position)
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        if (viewHolder is BillAdapter.HeaderFooterViewHolder) return 0
        return super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun getDragDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        if (viewHolder is BillAdapter.HeaderFooterViewHolder) return 0
        return super.getDragDirs(recyclerView, viewHolder)
    }
}