package ir.rainday.easylist

import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import java.lang.ref.WeakReference

/**
 * Created by mostafa-taghipour on 12/26/17.
 */
class DragAndSwipeItemTouchHelperCallback(callback: Callback) : ItemTouchHelper.Callback() {

    private var callback: WeakReference<Callback>? = null

    init {
        this.callback = WeakReference(callback)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return this.callback?.get()?.isLongPressDragEnabled() ?: true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return this.callback?.get()?.isItemViewSwipeEnabled() ?: true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // Set movement flags based on the layout manager
        val dragFlags = callback?.get()?.getDragDirections() ?: 0
        val swipeFlags = callback?.get()?.getSwipeDirections() ?: 0
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, source: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        if (source.itemViewType != target.itemViewType) {
            return false
        }

        // Notify the adapter of the move
        callback?.get()?.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
        // Notify the adapter of the dismissal
        callback?.get()?.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            viewHolder.itemView.translationX = dX
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder != null) {
            callback?.get()?.onItemSelected(viewHolder)
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        callback?.get()?.onItemDeselect(viewHolder)
    }


    interface Callback {
        fun getDragDirections(): Int {
            return ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }

        fun getSwipeDirections(): Int {
            return ItemTouchHelper.END or ItemTouchHelper.START
        }

        fun onItemMove(fromPosition: Int, toPosition: Int)
        fun onItemDismiss(position: Int)
        fun onItemSelected(viewHolder: RecyclerView.ViewHolder) {}
        fun onItemDeselect(viewHolder: RecyclerView.ViewHolder) {}
        fun isLongPressDragEnabled(): Boolean {
            return true
        }

        fun isItemViewSwipeEnabled(): Boolean {
            return true
        }
    }
}