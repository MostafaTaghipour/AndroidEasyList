package ir.rainday.easylist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by mostafa-taghipour on 12/29/17.
 */
interface SectionedAdapter : GroupedAdapter {


    private val _footerType
        get() = 1111


    override fun _tryToGetSectionItemType(position: Int): Int? {
        return if (isFooter(position))
            _footerType
        else
            super._tryToGetSectionItemType(position)
    }


    override fun _tryToGenerateSectionViewHolder(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup): RecyclerView.ViewHolder? {
        return if (viewType == _footerType && this.getFooterLayout() != null) {
            generateFooterView(inflater, viewType, viewGroup)
        } else
            super._tryToGenerateSectionViewHolder(inflater, viewType, viewGroup)
    }


    override fun _tryToBindSectionView(item: Any, position: Int, holder: RecyclerView.ViewHolder) {
        if (isFooter(position))
            this.bindFooterView(item, position, holder)
        else
            super._tryToBindSectionView(item, position, holder)
    }


    // must implements methods
    fun isFooter(position: Int): Boolean = false
    fun getFooterLayout(): Int? = null
    fun bindFooterView(item: Any, position: Int, holder: RecyclerView.ViewHolder){}
    fun generateFooterView(inflater: LayoutInflater, viewType: Int, viewGroup: ViewGroup) : RecyclerView.ViewHolder {
        val viewRetry = inflater.inflate(this.getFooterLayout()!!, viewGroup, false)
       return GenericViewHolder(viewRetry)
    }
}

