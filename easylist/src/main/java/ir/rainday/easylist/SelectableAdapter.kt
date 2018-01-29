package ir.rainday.easylist

/**
 * Created by mostafa-taghipour on 12/26/17.
 */
interface SelectableAdapter {

    fun toggleSelection(pos: Int) {
        if (adapter._private_selectedItems.get(pos, false)) {
            adapter._private_selectedItems.delete(pos)
        } else {
            adapter._private_selectedItems.put(pos, true)
        }
        adapter.notifyItemChanged(pos)
    }

    fun clearSelections() {
        adapter._private_selectedItems.clear()
        adapter.notifyDataSetChanged()
    }

    fun getSelectedItemCount(): Int {
        return adapter._private_selectedItems.size()
    }

    fun getSelectedItems(): List<Int> {
        val items = ArrayList<Int>()
        (0 until adapter._private_selectedItems.size()).mapTo(items) { adapter._private_selectedItems.keyAt(it) }
        return items
    }


    // must implements methods
    val adapter: RecyclerViewAdapter<*>
}