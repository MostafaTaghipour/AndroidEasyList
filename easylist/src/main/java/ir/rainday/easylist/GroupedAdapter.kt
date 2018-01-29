package ir.rainday.easylist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by mostafa-taghipour on 12/22/17.
 */


interface GroupedAdapter {

    val _headerType
        get() = 1110


    fun _tryToGetSectionItemType(position: Int): Int? {
        return when {
            isHeader(position) -> _headerType
            else -> null
        }
    }


    fun _tryToGenerateSectionViewHolder(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup): RecyclerView.ViewHolder? {
        if (viewType == _headerType) {
            return generateHeaderView(inflater, viewType, viewGroup)
        }

        return null
    }


    fun _tryToBindSectionView(item: Any, position: Int, holder: RecyclerView.ViewHolder) {
        if (isHeader(position))
            this.bindHeaderView(item, position, holder)
    }


    // must implements methods
    fun isHeader(position: Int): Boolean
    fun getHeaderLayout(): Int
    fun bindHeaderView(item: Any, position: Int, holder: RecyclerView.ViewHolder)
    fun generateHeaderView(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup): RecyclerView.ViewHolder {
        val headerView = inflater.inflate(this.getHeaderLayout(), viewGroup, false)
        return GenericViewHolder(headerView)
    }

}



