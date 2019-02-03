package ir.rainyday.easylist

import androidx.recyclerview.widget.DiffUtil


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


        return if (oldItem is Diffable && newItem is Diffable)
            oldItem.diffableIdentity == newItem.diffableIdentity
        else
            oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return if (oldItem is Diffable && newItem != null)
            oldItem.isEqualTo(newItem)
        else
            oldItem == newItem
    }

}


interface Diffable {
    val diffableIdentity: String
    fun isEqualTo(other: Any): Boolean {
        return this == other
    }
}

