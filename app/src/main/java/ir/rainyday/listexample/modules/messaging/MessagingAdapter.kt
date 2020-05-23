package ir.rainyday.listexample.modules.messaging

import android.content.Context
import androidx.core.content.ContextCompat
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.rainyday.easylist.RecyclerViewAdapter
import ir.rainyday.easylist.GenericViewHolder
import ir.rainyday.listexample.R
import ir.rainyday.listexample.model.Message
import ir.rainyday.listexample.model.me


/**
 * Created by mostafa-taghipour on 12/24/17.
 */
class MessagingAdapter(context: Context) : RecyclerViewAdapter<Message>() {

    companion object {
        private const val VIEW_TYPE_MESSAGE_SENT = 1
        private const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }


    override fun getLayout(viewType: Int): Int {
        return if (viewType == VIEW_TYPE_MESSAGE_SENT)
            R.layout.item_message_sent
        else
            R.layout.item_message_received
    }

    override fun bindView(item: Message, position: Int, viewHolder: RecyclerView.ViewHolder) {
       viewHolder as GenericViewHolder

        viewHolder.getView<TextView>(R.id.text_message_body)?.text = item.message
        viewHolder.getView<TextView>(R.id.text_message_time)?.text = ir.rainyday.listexample.DateUtils.formatDateTime(item.createdAt)
        viewHolder.getView<TextView>(R.id.text_message_name)?.text = item.sender.nickname
        viewHolder.getView<ImageView>(R.id.image_message_profile)?.setImageDrawable(ContextCompat.getDrawable(viewHolder.itemView.context, item.sender.avatar))


    }

    override fun getItemType(position: Int): Int {
        return if (items!![position].sender == me) VIEW_TYPE_MESSAGE_SENT else VIEW_TYPE_MESSAGE_RECEIVED
    }

}

