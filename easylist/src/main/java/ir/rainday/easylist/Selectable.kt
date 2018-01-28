package ir.rainday.easylist

/**
 * Created by mostafa-taghipour on 12/26/17.
 */
interface Selectable {

    fun toggleSelection(pos: Int) {
        if (adapter._selectedItems.get(pos, false)) {
            adapter._selectedItems.delete(pos)
        } else {
            adapter._selectedItems.put(pos, true)
        }
        adapter.notifyItemChanged(pos)
    }

    fun clearSelections() {
        adapter._selectedItems.clear()
        adapter.notifyDataSetChanged()
    }

    fun getSelectedItemCount(): Int {
        return adapter._selectedItems.size()
    }

    fun getSelectedItems(): List<Int> {
        val items = ArrayList<Int>()
        (0 until adapter._selectedItems.size()).mapTo(items) { adapter._selectedItems.keyAt(it) }
        return items
    }


    // must implements methods
    val adapter: RecyclerViewAdapter<*>
}