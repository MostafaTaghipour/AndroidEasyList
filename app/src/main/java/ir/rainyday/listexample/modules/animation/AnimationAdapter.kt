package ir.rainyday.listexample.modules.animation

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.rainyday.easylist.RecyclerViewAdapter
import ir.rainyday.easylist.GenericViewHolder
import ir.rainyday.easylist.SelectableAdapter
import ir.rainyday.listexample.R
import ir.rainyday.listexample.model.DateModel


/**
 * Created by mostafa-taghipour on 12/24/17.
 */
class AnimationAdapter(
        context: Context,
        onRowClickListener: GenericViewHolder.OnItemClicked<DateModel>) :
        RecyclerViewAdapter<DateModel>(),
        SelectableAdapter {

    private var enterAnimationFinished = false
    var lastVisibleItem = 100
    var listener: AnimationAdapterCallback? = null
    var actionMode:Boolean=false

    init {
        onItemClickListener = onRowClickListener
    }

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_date
    }

    override fun bindView(item: DateModel, position: Int, viewHolder: RecyclerView.ViewHolder) {
       viewHolder as GenericViewHolder


        val mDate: TextView? = viewHolder.getView(R.id.date)
        val editButton: View? = viewHolder.getView(R.id.editButton)
        mDate?.text = item.date.toString()

        editButton?.setOnClickListener(null)
        editButton?.setOnClickListener {
            listener?.update(item, position)
        }
        editButton?.visibility = if (actionMode) View.VISIBLE else View.INVISIBLE

        //val rootView = viewHolder.getView<View>(0)!!
        //setAnimation(rootView!!, position)

    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position <= lastVisibleItem && !enterAnimationFinished) {

            val delay = position * 50
            viewToAnimate.post {
                viewToAnimate.alpha = 0f
                viewToAnimate.translationY = 100f


                viewToAnimate
                        .animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(200)
                        .setStartDelay(delay.toLong())
                        .start()
            }

        }

        if (position > lastVisibleItem)
            enterAnimationFinished = true

    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as GenericViewHolder).clearAnimation()
        super.onViewDetachedFromWindow(holder)
    }

    override val adapter: RecyclerViewAdapter<*> = this
}

interface AnimationAdapterCallback {
    fun update(item: DateModel, position: Int)
}