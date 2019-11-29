package ir.rainyday.easylist

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.util.*


/**
 * Created by Plumillon Forge.
 */
@Suppress("UNCHECKED_CAST")
abstract class RecyclerViewAdapter<T : Any> constructor(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //region private variables
    internal val _private_selectedItems: SparseBooleanArray = SparseBooleanArray()
    internal var _private_allItems: MutableList<Any> = mutableListOf()
    internal var _private_lockNonefilteredItems = false
    internal var _private_nonefilteredItems: List<Any> = ArrayList()
    internal val _private_expandMap = HashMap<Any, List<Any>>()
    //endregion

    //region data setter and getter
    private val pendingUpdates: Queue<List<T>> = ArrayDeque()

    var items: List<T>?
        set(value) {
            setNewItems(value)
        }
        get() {
            return getPureItems()
        }


    private fun setNewItems(value: List<T>?) {
        var newList: List<T> = ArrayList(value ?: ArrayList())

        if (this is CollapsibleAdapter)
            newList = this._tryToMapList(newList) as List<T>

        if (this is FilterableAdapter && !_private_lockNonefilteredItems)
            _private_nonefilteredItems = newList

        if (isAnimationEnabled) {
            updateItemsInternal(newList)
        } else {

            val res = mutableListOf<Any>()
            res.addAll(newList)
            _private_allItems = res

//            _private_allItems.clear()
//            _private_allItems.addAll(newList)
            notifyDataSetChanged()
        }
    }

    private fun updateItemsInternal(newItems: List<T>, useSeparateThread: Boolean = true) {

        pendingUpdates.add(newItems)

        val oldItems = ArrayList(getPureItems() ?: ArrayList<T>())

        if (useSeparateThread) {
            runAsync {
                val diffResult = DiffUtil.calculateDiff(DiffCallback(oldItems, newItems))
                runOnUiThread {
                    applyDiffResult(newItems, diffResult, true)
                }
            }
        }
        else{
            val diffResult = DiffUtil.calculateDiff(DiffCallback(oldItems, newItems))
            applyDiffResult(newItems, diffResult, false)
        }
    }

    private fun applyDiffResult(newItems: List<T>,
                                diffResult: DiffUtil.DiffResult,
                                useSeparateThread: Boolean) {
        dispatchUpdates(newItems, diffResult)
        pendingUpdates.remove()
        if (!pendingUpdates.isEmpty()) {
            updateItemsInternal(pendingUpdates.peek(),useSeparateThread)
        }
    }

    private fun dispatchUpdates(newItems: List<T>,
                                diffResult: DiffUtil.DiffResult) {

        val res = mutableListOf<Any>()
        res.addAll(newItems)
        _private_allItems = res

//        _private_allItems.clear()
//        _private_allItems.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    private fun getPureItems(): List<T>? {
        var res: List<Any> = _private_allItems
        if (this is LoadingFooterAdapter)
            res = _tryToFilterItems(res)

        return res as? List<T>
    }
    //endregion

    //region public properties
    var isAnimationEnabled: Boolean = true

    val isEmpty: Boolean
        get() = itemCount == 0

    var onItemClickListener: GenericViewHolder.OnItemClicked<T>? = null
    //endregion

    //region override
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
    //endregion

    //region open for override
    open fun generateViewHolder(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val view: View = inflater.inflate(getLayout(viewType), viewGroup, false)
        val viewHolder = GenericViewHolder(view)

        if (onItemClickListener != null)
            view.setOnClickListener { _ ->
                items?.getOrNull(viewHolder.adapterPosition)?.let {
                    this.onItemClickListener?.onRecyclerViewItemClicked(this, view, viewHolder.adapterPosition, it)
                }
            }
        return viewHolder
    }

    open fun getItemType(position: Int): Int = 0
    //endregion

    //region must implement
    protected abstract fun getLayout(viewType: Int): Int

    protected abstract fun bindView(item: T, position: Int, viewHolder: RecyclerView.ViewHolder)
    //endregion
}



