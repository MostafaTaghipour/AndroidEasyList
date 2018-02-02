package ir.rainday.easylist

import android.content.Context
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlin.collections.HashMap


/**
 * Created by Plumillon Forge.
 */
@Suppress("UNCHECKED_CAST")
abstract class RecyclerViewAdapter<T : Any> constructor(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

   internal val _private_selectedItems: SparseBooleanArray = SparseBooleanArray()
    internal  val _private_allItems: MutableList<Any> = ArrayList()
    internal var _private_lockNonefilteredItems = false
    internal var _private_nonefilteredItems: List<Any> = ArrayList()
    internal val _private_expandMap = HashMap<Any, List<Any>>()

    var items: List<T>?
        set(value) {

            var newList: List<T> = value ?: ArrayList()
            val oldList: List<T> = items ?: ArrayList<T>()

            if (this is CollapsibleAdapter)
                newList = this._tryToMapList(newList) as List<T>

            if (this is FilterableAdapter && !_private_lockNonefilteredItems)
                _private_nonefilteredItems = newList


            if (isAnimationEnabled) {
                val diffResult = DiffUtil.calculateDiff(DiffCallback(oldList, newList))

                _private_allItems.clear()
                _private_allItems.addAll(newList)

                diffResult.dispatchUpdatesTo(this)
            } else {
                _private_allItems.clear()
                _private_allItems.addAll(newList)
                notifyDataSetChanged()
            }
        }
        get() {

            var res: List<Any> = _private_allItems
            if (this is LoadingFooterAdapter)
                res = _tryToFilterItems(res)

            return res as? List<T>
        }

    var isAnimationEnabled: Boolean = true

    val isEmpty: Boolean
        get() = itemCount == 0

    var onItemClickListener: GenericViewHolder.OnItemClicked<T>? = null


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(context)
        var viewHolder: RecyclerView.ViewHolder? = null

        if (this is LoadingFooterAdapter)
            viewHolder = this._tryToGenerateLoadingViewHolder(inflater, viewType, viewGroup)

        if (this is GroupedAdapter)
            viewHolder = this._tryToGenerateSectionViewHolder(inflater, viewType, viewGroup)

        if (viewHolder == null) {

            viewHolder = generateViewHolder(inflater, viewType, viewGroup)
        }

        return viewHolder
    }

    open fun generateViewHolder(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view: View = inflater.inflate(getLayout(viewType), viewGroup, false)
        val viewHolder = GenericViewHolder(view)

        if (onItemClickListener != null)
            view.setOnClickListener {
                items?.getOrNull(viewHolder.adapterPosition)?.let {
                    this.onItemClickListener?.onRecyclerViewItemClicked(this, view, viewHolder.adapterPosition, it)
                }
            }
        return viewHolder
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = _private_allItems[position]

        if (this is LoadingFooterAdapter)
            this._tryToBindLoadingView(item, holder)

        if (this is GroupedAdapter)
            this._tryToBindSectionView(item, position, holder)

        try {
            bindView(item as T, position, holder)

            if (this is SelectableAdapter) {
                // change the row state to activated
                holder.itemView.isActivated = _private_selectedItems.get(position, false)
            }

        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
        return _private_allItems.size
    }

    override fun getItemViewType(position: Int): Int {
        var type = (this as? LoadingFooterAdapter)?._tryToGetLoadingItemType(position)

        if (type == null)
            type = (this as? GroupedAdapter)?._tryToGetSectionItemType(position)

        if (type == null)
            type = getItemType(position)

        return type
    }

    // must implements methods
    protected abstract fun getLayout(viewType: Int): Int

    protected abstract fun bindView(item: T, position: Int, viewHolder: RecyclerView.ViewHolder)
    open fun getItemType(position: Int): Int = 0

}



