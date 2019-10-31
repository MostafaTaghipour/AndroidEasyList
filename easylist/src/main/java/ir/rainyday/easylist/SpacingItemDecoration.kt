package ir.rainyday.easylist

import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by taghipour on 11/03/2018.
 */


class SpacingItemDecoration @JvmOverloads constructor(
    private val spacing: Int,
    private val includeEdges: Boolean = false
) : RecyclerView.ItemDecoration() {

    private var displayMode: Int = -1
    private var isRTL: Boolean? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager ?: return

        if (isRTL == null)
            isRTL = ViewCompat.getLayoutDirection(parent) == ViewCompat.LAYOUT_DIRECTION_RTL

        val position = parent.getChildViewHolder(view).adapterPosition
        val itemCount = state.itemCount
        setSpacingForDirection(outRect, view, layoutManager, position, itemCount)
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        view: View,
        layoutManager: RecyclerView.LayoutManager,
        position: Int,
        itemCount: Int
    ) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager)
        }

        when (displayMode) {
            HORIZONTAL -> {
                if (isRTL == true) {
                    outRect.right = if (position != 0 || includeEdges) spacing else 0
                    outRect.left = if (position == itemCount - 1 && includeEdges) spacing else 0
                } else {
                    outRect.left = if (position != 0 || includeEdges) spacing else 0
                    outRect.right = if (position == itemCount - 1 && includeEdges) spacing else 0
                }
            }
            VERTICAL -> {
                outRect.top = if (position != 0 || includeEdges) spacing else 0
                outRect.bottom = if (position == itemCount - 1 && includeEdges) spacing else 0
            }
            GRID -> if (layoutManager is androidx.recyclerview.widget.GridLayoutManager) {
                val lp: GridLayoutManager.LayoutParams = view.layoutParams as GridLayoutManager.LayoutParams
                val spanCount = layoutManager.spanCount
                val spanIndex = lp.spanIndex
                val spanSize = lp.spanSize

                if (isRTL == true) {

                    outRect.right =
                            if (spanIndex == 0 && !includeEdges) 0 else (spacing * ((spanCount - spanIndex) / spanCount.toFloat())).toInt()
                    outRect.left =
                            if (spanIndex == spanCount - 1 && !includeEdges) 0 else (spacing * ((spanIndex + spanSize) / spanCount.toFloat())).toInt()

                } else {
                    outRect.left =
                            if (spanIndex == 0 && !includeEdges) 0 else (spacing * ((spanCount - spanIndex) / spanCount.toFloat())).toInt()
                    outRect.right =
                            if (spanIndex == spanCount - 1 && !includeEdges) 0 else (spacing * ((spanIndex + spanSize) / spanCount.toFloat())).toInt()

                }
                outRect.bottom = if (!includeEdges) 0 else spacing
                outRect.top =
                        if (includeEdges && position < spanCount) spacing else if (!includeEdges && position >= spanCount) spacing else 0
            }

            STAGGERED_GRID -> if (layoutManager is androidx.recyclerview.widget.StaggeredGridLayoutManager) {

                val lp: StaggeredGridLayoutManager.LayoutParams =
                    view.layoutParams as StaggeredGridLayoutManager.LayoutParams

                val spanCount = layoutManager.spanCount
                val spanIndex = lp.spanIndex
                val spanSize = if (lp.isFullSpan) layoutManager.spanCount else 1

                outRect.left =
                        if (spanIndex == 0 && !includeEdges) 0 else (spacing * ((spanCount - spanIndex) / spanCount.toFloat())).toInt()
                outRect.right =
                        if (spanIndex == spanCount - 1 && !includeEdges) 0 else (spacing * ((spanIndex + spanSize) / spanCount.toFloat())).toInt()


                outRect.bottom = if (!includeEdges) 0 else spacing
                outRect.top =
                        if (includeEdges && position < spanCount) spacing else if (!includeEdges && position >= spanCount) spacing else 0
            }
        }

    }

    private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager): Int {
        return when {
            layoutManager is GridLayoutManager -> GRID
            layoutManager is StaggeredGridLayoutManager -> STAGGERED_GRID
            layoutManager.canScrollHorizontally() -> HORIZONTAL
            else -> VERTICAL
        }
    }

    companion object {
        private  const val HORIZONTAL = 0
        private const val VERTICAL = 1
        private const val GRID = 2
        private const val STAGGERED_GRID = 3
    }
}


fun RecyclerView.setItemSpacing(
    spacing: Int,
    includeEdges: Boolean = false
) {
    this.addItemDecoration(SpacingItemDecoration(spacing, includeEdges))
}
