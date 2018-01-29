package ir.rainday.easylist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by mostafa-taghipour on 12/22/17.
 */

interface LoadingFooterAdapter {
    private val _loadingToken
        get() = "loading_token"

    private val _retryToken
        get() = "retry_token"


    private val _loadingType
        get() = 1110


    private val _retryType
        get() = 1111


    var loading: Boolean
        set(value) {
            if (value) {
                if (loading)
                    return

                retry = false

                if (adapter.itemCount == 0)
                    return

                adapter._private_allItems.add(adapter._private_allItems.size, _loadingToken)
                adapter.notifyItemInserted(adapter._private_allItems.size - 1)

            } else {
                if (loading) {

                    val position = adapter._private_allItems.size - 1
                    adapter._private_allItems.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }
        }
        get() {
            return !adapter._private_allItems.isEmpty() && (adapter._private_allItems.last() as? String) == _loadingToken
        }


    var retry: Boolean
        set(value) {

            if (getRetryLayout() == null)
                return

            if (value) {
                if (retry)
                    return

                loading = false

                if (adapter.itemCount == 0)
                    return

                adapter._private_allItems.add(adapter._private_allItems.size, _retryToken)
                adapter.notifyItemInserted(adapter._private_allItems.size - 1)

            } else {
                if (retry) {

                    val position = adapter._private_allItems.size - 1
                    adapter._private_allItems.removeAt(position)
                    adapter.notifyItemRemoved(position)
                }
            }
        }
        get() {
            return !adapter._private_allItems.isEmpty() && (adapter._private_allItems.last() as? String) == _retryToken
        }



    private fun _isRetry(item: Any): Boolean {
        return item is String && item == _retryToken
    }

    fun _isLoading(item: Any): Boolean {
        return item is String && item == _loadingToken
    }


    fun _tryToGetLoadingItemType(position: Int): Int? {
        return when {
            (adapter._private_allItems[position] as? String) == _loadingToken -> _loadingType
            (adapter._private_allItems[position] as? String) == _retryToken -> _retryType
            else -> null
        }
    }

    fun _tryToGenerateLoadingViewHolder(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup):  RecyclerView.ViewHolder ? {
        if (viewType==_loadingType) {
           return generateLoadingView(inflater, viewType, viewGroup)
        } else if (viewType==_retryType && this.getRetryLayout() != null) {
           return generateRetryView(inflater, viewType, viewGroup)
        }

        return null
    }


    fun _tryToBindLoadingView(item: Any, holder: RecyclerView.ViewHolder) {
        if (_isLoading(item))
            this.bindLoadingView(holder)
        else if (_isRetry(item))
            this.bindRetryView(holder)
    }



    fun _tryToFilterItems(res: List<Any>): List<Any> {
        return res.filter {!this._isLoading(it) }
    }



    // must implements methods
    val adapter: RecyclerViewAdapter<*>
    fun getLoadingLayout(): Int
    fun getRetryLayout(): Int? = null
    fun bindLoadingView(viewHolder: RecyclerView.ViewHolder) {}
    fun bindRetryView(viewHolder: RecyclerView.ViewHolder) {}
    fun generateLoadingView(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup) : RecyclerView.ViewHolder {
        val viewLoading = inflater.inflate(this.getLoadingLayout(), viewGroup, false)
        return GenericViewHolder(viewLoading)
    }
    fun generateRetryView(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup) : RecyclerView.ViewHolder {
        val viewRetry = inflater.inflate(this.getRetryLayout()!!, viewGroup, false)
        return GenericViewHolder(viewRetry)
    }
}