package ir.rainday.easylist

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference

/**
 * Created by mostafa-taghipour on 12/23/17.
 */


class RecyclerViewEmptyObserver(recyclerView: RecyclerView) : RecyclerView.AdapterDataObserver() {

    private var weakRecyclerView: WeakReference<RecyclerView>? = null
    private var weakEmptyView: WeakReference<View>? = null

    var emptyView: View?
        get() = weakEmptyView?.get()
        set(value) {
            if (value == null)
                return

            weakEmptyView = WeakReference(value)
            checkIfEmpty()
        }

    init {
        this.weakRecyclerView = WeakReference(recyclerView)
    }

    constructor(recyclerView: RecyclerView, emptyView: View) : this(recyclerView) {
        this.emptyView = emptyView
    }


    constructor(recyclerView: RecyclerView, @LayoutRes emptyViewRes: Int) : this(recyclerView) {
        val contentView = recyclerView.parent as ViewGroup
        val view = (recyclerView.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(emptyViewRes, null, false)
        view?.layoutParams = recyclerView.layoutParams
        contentView.addView(view)
        emptyView = view
    }

    /**
     * Check if Layout is empty and show the appropriate view
     */
    private fun checkIfEmpty() {
        val empty = weakEmptyView?.get()
        val recyclerView = weakRecyclerView?.get()
        recyclerView?.adapter?.itemCount?.let {
            empty?.visibility = if (it == 0) View.VISIBLE else View.INVISIBLE
        }
    }


    /**
     * Abstract method implementations
     */
    override fun onChanged() {
        checkIfEmpty()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        checkIfEmpty()
    }

}


fun RecyclerView.setEmptyView(emptyView: View) {
    this.adapter.registerAdapterDataObserver(RecyclerViewEmptyObserver(this, emptyView))
}

fun RecyclerView.setEmptyView(@LayoutRes emptyViewRes: Int) {
    this.adapter.registerAdapterDataObserver(RecyclerViewEmptyObserver(this, emptyViewRes))
}