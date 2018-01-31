package ir.rainday.easylist

import android.widget.Filter
import android.widget.Filterable
import ir.rainday.easylist.RecyclerViewAdapter
import java.util.ArrayList

/**
 * Created by mostafa-taghipour on 12/22/17.
 */


interface FilterableAdapter : Filterable {

    private val adapter: RecyclerViewAdapter<*>
        get() = this as RecyclerViewAdapter<*>

    override fun getFilter(): Filter {
        return object : Filter() {

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                (adapter as? RecyclerViewAdapter<Any>)?.let {
                    it._private_lockNonefilteredItems = true
                    it.items = results.values as? List<Any>?
                    it._private_lockNonefilteredItems = false
                }
            }

            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val filteredResults = if (constraint.isNullOrEmpty()) {
                    adapter._private_nonefilteredItems
                } else {
                    getFilteredResults(constraint!!)
                }

                val results = FilterResults()
                results.values = filteredResults

                return results
            }
        }


    }


    private fun getFilteredResults(constraint: CharSequence): List<Any>? {
        val results = ArrayList<Any>()

        adapter._private_nonefilteredItems
                .filter { filterItem(constraint, it) }
                .forEach { results.add(it) }
        return results
    }

    fun setFilterConstraint(constraint: CharSequence?) {
        filter.filter(constraint)
    }

    // must implements methods
    fun filterItem(constraint: CharSequence, item: Any): Boolean
}