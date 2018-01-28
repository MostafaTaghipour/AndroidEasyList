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

    val _selectedItems: SparseBooleanArray = SparseBooleanArray()
    val _allItems: MutableList<Any> = ArrayList()
    val _expandMap = HashMap<Any, List<Any>>()

    var items: List<T>?
        set(value) {
            var newList: List<T> = value ?: return


            if (this is Expandable)
                newList = this._tryToMapList(newList) as List<T>


            val diffResult = DiffUtil.calculateDiff(DiffCallback(items ?: ArrayList<T>(), newList))

            _allItems.clear()
            _allItems.addAll(newList)

            diffResult.dispatchUpdatesTo(this)
        }
        get() {

            var res : List<Any> = _allItems
            if (this is LoadingFooter)
                res = _tryToFilterItems(res)

            return res as? List<T>
        }


    val isEmpty: Boolean
        get() = itemCount == 0


    var onItemClickListener: GenericViewHolder.OnItemClicked<T>? = null

    @Suppress("CanBeVal")
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(context)
        var viewHolder: RecyclerView.ViewHolder? = null

        if (this is LoadingFooter)
            viewHolder = this._tryToGenerateLoadingViewHolder(inflater, viewType, viewGroup)

        if (this is Grouped)
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
                this.onItemClickListener?.onRowClicked(view, viewHolder.adapterPosition, items?.get(viewHolder.adapterPosition))
            }
        return viewHolder
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = _allItems[position]

        if (this is LoadingFooter)
            this._tryToBindLoadingView(item, holder)

        if (this is Grouped)
            this._tryToBindSectionView(item, position, holder)

        try {
            bindView(item as T, position, holder)

            if (this is Selectable) {
                // change the row state to activated
                holder.itemView.isActivated = _selectedItems.get(position, false)
            }

        } catch (e: Exception) {
        }
    }


    override fun getItemCount(): Int {
        return _allItems.size
    }

    override fun getItemViewType(position: Int): Int {
        var type = (this as? LoadingFooter)?._tryToGetLoadingItemType(position)

        if (type == null)
            type = (this as? Grouped)?._tryToGetSectionItemType(position)

        if (type == null)
            type = getItemType(position)

        return type
    }


    // must implements methods
    protected abstract fun getLayout(viewType: Int): Int
    protected abstract fun bindView(item: T, position: Int, viewHolder: RecyclerView.ViewHolder)
    open fun getItemType(position: Int): Int = 0
}



