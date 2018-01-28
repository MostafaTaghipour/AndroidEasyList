package ir.rainday.easylist

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

/**
 * Created by mostafa-taghipour on 12/21/17.
 */
abstract class PaginatedRecyclerOnScrollListener(private val mLinearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0 // The total number of _allItems in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.

    var visibleThreshold = 5 // The minimum amount of _allItems to have below your current scroll position before loading more.

    var lastLoadedPage=0
        private set

    var currentPage = 0
        private set

    var pageSize: Int? = null

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

       val visibleItemCount = recyclerView!!.childCount
      val totalItemCount = mLinearLayoutManager.itemCount
       val firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()

        val lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition()
        if (pageSize != null && lastVisibleItemPosition / pageSize!! != currentPage) {
            currentPage = lastVisibleItemPosition / pageSize!!
            onPageChanged(currentPage)
        }


        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            // End has been reached

            lastLoadedPage++
            onLoadMore(lastLoadedPage)

            loading = true
        }
    }

    open fun onPageChanged(currentPage: Int) {}

    abstract fun onLoadMore(page: Int)

    fun reset() {
        previousTotal = 0
        loading = true
        currentPage=0
        lastLoadedPage=0
    }
}