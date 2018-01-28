package ir.rainday.easylist

import android.support.v7.widget.RecyclerView
import android.view.View
import java.lang.ref.WeakReference

/**
 * Created by mostafa-taghipour on 12/23/17.
 */


class RecyclerViewEmptyObserver(recyclerView: RecyclerView, emptyView: View) : RecyclerView.AdapterDataObserver() {

    private var recyclerView: WeakReference<RecyclerView>?=null
    private var emptyView: WeakReference<View>?=null

    init {
        this.recyclerView= WeakReference(recyclerView)
        this.emptyView= WeakReference(emptyView)
        checkIfEmpty()
    }


    /**
     * Check if Layout is empty and show the appropriate view
     */
    private fun checkIfEmpty() {
        val emptyView = emptyView?.get()
        val recyclerView = recyclerView?.get()
        if (emptyView != null && recyclerView?.adapter != null) {
            val emptyViewVisible = recyclerView.adapter.itemCount == 0
            emptyView.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
//            recyclerView.visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
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
