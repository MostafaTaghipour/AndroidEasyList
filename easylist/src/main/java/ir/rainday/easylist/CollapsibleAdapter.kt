package ir.rainday.easylist

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by mostafa-taghipour on 12/29/17.
 */
interface CollapsibleAdapter : GroupedAdapter {

    private val adapter: RecyclerViewAdapter<*>
        get() = this as RecyclerViewAdapter<*>

    override fun isHeader(position: Int): Boolean {
        return isHeader(adapter.items!![position])
    }

    fun _tryToMapList(newList: List<Any>): List<Any> {

        val res = ArrayList<Any>()
        val map = HashMap<Any, List<Any>>()
        val headers = newList.filter { isHeader(it) }
        for (header in headers) {

            res.add(header)
            val position = newList.indexOf(header)
            val items = ArrayList<Any>()
            for (i in position + 1 until newList.size) {

                val item = newList[i]

                if (isHeader(item))
                    break

                items.add(item)

                if (type == Type.NORMAL) {
                    if (item in adapter._private_allItems || !collapseByDefault)
                        res.add(item)
                } else {
                    if (adapter._private_allItems.count { !isHeader(it) } == 0) {
                        if (position == 0)
                            res.add(item)
                    } else {
                        if (item in adapter._private_allItems)
                            res.add(item)
                    }
                }
            }

            map[header] = items
        }

        adapter._private_expandMap.clear()
        adapter._private_expandMap.putAll(map)
        return res
    }


    override fun bindHeaderView(item: Any, position: Int, holder: RecyclerView.ViewHolder) {
        bindHeaderView(item, position, holder, isExpanded(position))
    }

    override fun generateHeaderView(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val headerView = inflater.inflate(this.getHeaderLayout(), viewGroup, false)
        val viewHolder = GenericViewHolder(headerView)

        headerView.setOnClickListener {
            toggleState(viewHolder.adapterPosition)
        }
        return viewHolder
    }


    fun toggleState(position: Int) {
        if (type == Type.NORMAL) {
            if (isExpanded(position)) {
                collapse(position)

            } else {
                expand(position)
            }
        } else {
            if (isExpanded(position))
                return

            adapter._private_allItems[position].let {
                collapseAll()
                Handler().postDelayed({
                    expand(adapter._private_allItems.indexOf(it))
                }, 200)

            }

        }
    }

    fun isExpanded(position: Int): Boolean {
        return !isCollapsed(position)
    }

    fun isCollapsed(position: Int): Boolean {

        if (position + 1 >= adapter._private_allItems.size)
            return true

        return isHeader(adapter._private_allItems[position + 1])
    }

    fun expand(position: Int) {

        if ((position >= adapter._private_allItems.size) or isExpanded(position))
            return

        onExpand(position)
        adapter._private_expandMap[adapter._private_allItems[position]]?.let {
            val index = position + 1
            adapter._private_allItems.addAll(index, it)
            adapter.notifyItemRangeInserted(index, it.size)
        }

    }


    fun collapse(position: Int) {

        if ((position >= adapter._private_allItems.size) or isCollapsed(position))
            return

        onCollapse(position)
        adapter._private_expandMap[adapter._private_allItems[position]]?.let {
            adapter._private_allItems.removeAll(it)
            adapter.notifyItemRangeRemoved(position + 1, it.size)
        }
    }


    fun collapseAll(exceptPosition: Int? = null) {
        adapter._private_allItems
                .filter { isHeader(it) }
                .map { adapter._private_allItems.indexOf(it) }
                .filter { it != exceptPosition }
                .forEach { collapse(it) }
    }

    fun expandAll(exceptPosition: Int? = null) {
        adapter._private_allItems
                .filter { isHeader(it) }
                .map { adapter._private_allItems.indexOf(it) }
                .filter { it != exceptPosition }
                .forEach { expand(it) }
    }




    // must implements methods
    val collapseByDefault: Boolean
        get() = true
    val type: Type
        get() = Type.NORMAL

    fun isHeader(item: Any): Boolean
    fun bindHeaderView(item: Any, position: Int, holder: RecyclerView.ViewHolder, isExpanded: Boolean)
    fun onExpand(position: Int) {}
    fun onCollapse(position: Int) {}


    enum class Type {
        NORMAL,
        ACCORDION
    }
}