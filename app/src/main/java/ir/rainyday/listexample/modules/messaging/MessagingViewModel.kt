package ir.rainyday.listexample.modules.messaging

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.Editable
import ir.rainyday.listexample.model.Message
import ir.rainyday.listexample.model.me
import ir.rainyday.listexample.model.sarah
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by mostafa-taghipour on 11/29/17.
 */
class MessagingViewModel() : ViewModel() {

    val items = MutableLiveData<MutableList<Message>>()

    init {
        //init service and load data
        items.value = mutableListOf<Message>()
        loadMessages()
    }

    private fun loadMessages() {
        val list = ArrayList<Message>()
        list.add( Message("can you borrow me your book, i really need it \ni promise you give back it very soon.", sarah, Date().time))
        list.add( Message("hi sarah \ni'm fine", me, Date().time-200000))
        list.add( Message("hi john how are you ?", sarah, Date().time-300000))

        items.value=list
    }

    fun sendMessage(message: String) {
        val list = items.value
        list?.add(0, Message(message, me, Date().time))
        items.value = list
    }


}
