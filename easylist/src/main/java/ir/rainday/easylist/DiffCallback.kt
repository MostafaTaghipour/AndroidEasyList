package ir.rainday.easylist

import android.support.v7.util.DiffUtil


/**
 * Created by taghipour on 21/11/2017.
 */

class DiffCallback(private val oldList: List<*>, private val newList: List<*>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]


        if (oldItem is Diffable && newItem is Diffable)
            return oldItem.diffableIdentity == newItem.diffableIdentity

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem is Diffable && newItem != null)
            return oldItem.isEqualTo(newItem)


        return oldItem == newItem
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}


interface Diffable {
    val diffableIdentity: String
    fun isEqualTo(other: Any): Boolean {
       return this == other
    }
}

