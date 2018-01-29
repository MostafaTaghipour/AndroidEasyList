package ir.rainday.easylist

import android.support.v7.widget.RecyclerView
import android.view.View
import java.util.HashMap

/**
 * Created by taghipour on 21/11/2017.
 */
class GenericViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val views: MutableMap<Int, View>

    private val view: View?
        get() = getView(0)

    init {
        views = HashMap()
        views.put(0, view)
    }


    private fun initViewList(idList: IntArray) {
        for (id in idList)
            initViewById(id)
    }

    private fun initViewById(id: Int) {
        val view = if (view != null) view!!.findViewById<View>(id) else null

        if (view != null)
            views.put(id, view)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(id: Int): T? {
        if (views.containsKey(id))
            return views[id] as? T
        else
            initViewById(id)

        return views[id] as? T
    }


    fun clearAnimation() {
        view?.clearAnimation()
    }

    interface OnItemClicked<in T> {
        fun onRecyclerViewItemClicked(adapter: RecyclerView.Adapter<*>, view: View, position: Int, item: T)
    }
}
