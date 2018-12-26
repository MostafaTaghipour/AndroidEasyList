package ir.rainyday.listexample.modules.animation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.rainyday.listexample.model.DateModel
import java.util.*


/**
 * Created by mostafa-taghipour on 11/29/17.
 */
class AnimationViewModel() : ViewModel() {

    private var lastDeletedItem: Item? = null
    val items = MutableLiveData<MutableList<DateModel>>()

    init {
        //init service and load data
        items.value = mutableListOf<DateModel>()
        loadDates()
    }


    private fun loadDates() {

        for (i in 1..15) {
            var date = Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - i)
            date = Date(calendar.timeInMillis)
            val item = DateModel(date)
            items.value?.add(item)
        }
    }

    fun addNewItem() {
        val list = items.value
        val item = DateModel(Date())
        list?.add(0, item)
        items.value = list
    }

    fun update(date: Date?, position: Int) {

        if (date == null)
            return

        val list = items.value
        list!![position] = DateModel(date)
        items.value = list
    }

    fun removeItem(position: Int) {
        val list = items.value
        list?.get(position)?.let {
           lastDeletedItem=Item(it,position)
        }
        list?.removeAt(position)
        items.value = list
    }

    fun undoDelete(){
        lastDeletedItem?.let {
            val list = items.value
            list?.add(it.index, it.item)
            items.value = list
        }
    }

    fun removeItems(positions: List<Int>) {
        val list = items.value
        for (i in positions.indices.reversed()) {
            list?.removeAt(positions[i])
        }
        items.value = list
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        val list = items.value
        Collections.swap(list, fromPosition, toPosition)
        items.value = list
    }

    private data class Item(val item:DateModel,val index:Int)

}
