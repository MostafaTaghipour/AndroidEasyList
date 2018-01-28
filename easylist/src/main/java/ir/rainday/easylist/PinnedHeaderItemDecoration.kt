package ir.rainday.easylist

/**
 * Created by mostafa-taghipour on 12/22/17.
 */

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Region
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

import java.util.HashMap

@Suppress("DEPRECATION")
/**
 * ItemDecoration for Pinned Header.
 *
 * porting from https://github.com/beworker/pinned-section-listview
 * @author takahr@gmail.com
 */
class PinnedHeaderItemDecoration : RecyclerView.ItemDecoration() {

    private var mAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>? = null

    // cached data
    // pinned header view
    private var mPinnedHeaderView: View? = null
    private var mHeaderPosition = -1

    @SuppressLint("UseSparseArrays")
    private val mPinnedViewTypes = HashMap<Int, Boolean>()

    private var mPinnedHeaderTop: Int = 0
    private var mClipBounds: Rect? = null

    interface PinnedHeaderAdapter {
        fun isPinnedViewType(viewType: Int, position: Int): Boolean
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        createPinnedHeader(parent)

        if (mPinnedHeaderView != null) {
            // check overlap section view.
            //TODO support only vertical header currently.
            val headerEndAt = mPinnedHeaderView!!.top + mPinnedHeaderView!!.height
            val v = parent.findChildViewUnder((c.width / 2).toFloat(), (headerEndAt + 1).toFloat())

            if (isHeaderView(parent, v)) {
                mPinnedHeaderTop = v.top - mPinnedHeaderView!!.height
            } else {
                mPinnedHeaderTop = 0
            }

            mClipBounds = c.clipBounds
            mClipBounds!!.top = mPinnedHeaderTop + mPinnedHeaderView!!.height
            c.clipRect(mClipBounds!!)
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        if (mPinnedHeaderView != null) {
            c.save()

            mClipBounds!!.top = 0
            c.clipRect(mClipBounds!!, Region.Op.UNION)
            c.translate(0f, mPinnedHeaderTop.toFloat())
            mPinnedHeaderView!!.draw(c)

            c.restore()
        }
    }

    private fun createPinnedHeader(parent: RecyclerView) {
        checkCache(parent)

        // get LinearLayoutManager.
        val linearLayoutManager: LinearLayoutManager
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            linearLayoutManager = layoutManager
        } else {
            return
        }

        val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
        val headerPosition = findPinnedHeaderPosition(firstVisiblePosition)

        if (headerPosition >= 0 && mHeaderPosition != headerPosition) {
            mHeaderPosition = headerPosition
            val viewType = mAdapter!!.getItemViewType(headerPosition)

            val pinnedViewHolder = mAdapter!!.createViewHolder(parent, viewType)

            mAdapter!!.bindViewHolder(pinnedViewHolder, headerPosition)
            mPinnedHeaderView = pinnedViewHolder.itemView

            // read layout parameters
            var layoutParams: ViewGroup.LayoutParams? = mPinnedHeaderView!!.layoutParams
            if (layoutParams == null) {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                mPinnedHeaderView!!.layoutParams = layoutParams
            }

            var heightMode = View.MeasureSpec.getMode(layoutParams.height)
            var heightSize = View.MeasureSpec.getSize(layoutParams.height)

            if (heightMode == View.MeasureSpec.UNSPECIFIED) {
                heightMode = View.MeasureSpec.EXACTLY
            }

            val maxHeight = parent.height - parent.paddingTop - parent.paddingBottom
            if (heightSize > maxHeight) {
                heightSize = maxHeight
            }

            // measure & layout
            val ws = View.MeasureSpec.makeMeasureSpec(parent.width - parent.paddingLeft - parent.paddingRight, View.MeasureSpec.EXACTLY)
            val hs = View.MeasureSpec.makeMeasureSpec(heightSize, heightMode)
            mPinnedHeaderView!!.measure(ws, hs)
            mPinnedHeaderView!!.layout(0, 0, mPinnedHeaderView!!.measuredWidth, mPinnedHeaderView!!.measuredHeight)
        }
    }

    private fun findPinnedHeaderPosition(fromPosition: Int): Int {
        if (fromPosition > mAdapter!!.itemCount) {
            return -1
        }

        for (position in fromPosition downTo 0) {
            val viewType = mAdapter!!.getItemViewType(position)
            if (isPinnedViewType(viewType, position)) {
                return position
            }
        }

        return -1
    }

    private fun isPinnedViewType(viewType: Int, position: Int): Boolean {
        if (!mPinnedViewTypes.containsKey(viewType)) {
            mPinnedViewTypes.put(viewType, (mAdapter as PinnedHeaderAdapter).isPinnedViewType(viewType, position))
        }

        return mPinnedViewTypes[viewType]!!
    }

    private fun isHeaderView(parent: RecyclerView, v: View): Boolean {
        val position = parent.getChildPosition(v)
        if (position == RecyclerView.NO_POSITION) {
            return false
        }
        val viewType = mAdapter!!.getItemViewType(position)

        return isPinnedViewType(viewType, position)
    }

    private fun checkCache(parent: RecyclerView) {
        val adapter = parent.adapter
        if (mAdapter !== adapter) {
            disableCache()
            if (adapter is PinnedHeaderAdapter) {
                mAdapter = adapter
            } else {
                mAdapter = null
            }
        }
    }

    private fun disableCache() {
        mPinnedHeaderView = null
        mHeaderPosition = -1
        mPinnedViewTypes.clear()
    }

    companion object {
        private val TAG = PinnedHeaderItemDecoration::class.java.simpleName
    }

}