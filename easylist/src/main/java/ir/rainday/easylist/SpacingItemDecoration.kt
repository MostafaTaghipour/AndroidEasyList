package ir.rainday.easylist

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by taghipour on 11/03/2018.
 */


class SpacingItemDecoration @JvmOverloads constructor(
        private val spacing: Int,
        private val addSpaceBeforeFirstItem: Boolean = false,
        private val addSpaceAfterLastItem: Boolean = false
) : RecyclerView.ItemDecoration() {

    private var displayMode: Int = -1


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        val layoutManager = parent.layoutManager
        setSpacingForDirection(outRect, layoutManager!!, position, itemCount)
    }

    private fun setSpacingForDirection(outRect: Rect,
                                       layoutManager: RecyclerView.LayoutManager,
                                       position: Int,
                                       itemCount: Int) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager)
        }

        when (displayMode) {
            HORIZONTAL -> {
                outRect.left = if (position==0 && addSpaceBeforeFirstItem) spacing else 0
                outRect.right = if (position == itemCount - 1 && !addSpaceAfterLastItem) 0 else spacing
//                outRect.top = spacing
//                outRect.bottom = spacing
            }
            VERTICAL -> {
//                outRect.left = spacing
//                outRect.right = spacing

                outRect.top = if (position==0 && addSpaceBeforeFirstItem) spacing else 0
                outRect.bottom = if (position == itemCount - 1 && !addSpaceAfterLastItem) 0 else spacing
            }
            GRID -> if (layoutManager is androidx.recyclerview.widget.GridLayoutManager) {
                val cols = layoutManager.spanCount
                val rows = itemCount / cols

                outRect.left = spacing
                outRect.right = if (position % cols == cols - 1) spacing else 0
                outRect.top = spacing
                outRect.bottom = if (position / cols == rows - 1) spacing else 0
            }
        }

    }

    private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager): Int {
        if (layoutManager is androidx.recyclerview.widget.GridLayoutManager) return GRID
        return if (layoutManager.canScrollHorizontally()) HORIZONTAL else VERTICAL
    }

    companion object {

        val HORIZONTAL = 0
        val VERTICAL = 1
        val GRID = 2
    }
}


fun RecyclerView.setItemSpacing(
        spacing: Int,
        addSpaceBeforeFirstItem: Boolean = false,
        addSpaceAfterLastItem: Boolean = false
) {
    this.addItemDecoration(SpacingItemDecoration(spacing, addSpaceBeforeFirstItem, addSpaceAfterLastItem))
}