package ir.rainyday.listexample.modules.expandable

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.rainyday.easylist.CollapsibleAdapter
import ir.rainyday.easylist.GenericViewHolder
import ir.rainyday.easylist.RecyclerViewAdapter
import ir.rainyday.listexample.AppHelpers
import ir.rainyday.listexample.R
import ir.rainyday.listexample.model.Movie
import java.lang.ref.WeakReference

/**
 * Created by mostafa-taghipour on 12/22/17.
 */


class ExpandableAdapter(context: Context, layoutManager: LinearLayoutManager) : RecyclerViewAdapter<Any>(context), CollapsibleAdapter {

    private val weakLayoutManager: WeakReference<LinearLayoutManager> = WeakReference(layoutManager)

    override fun getLayout(viewType: Int): Int {
        return R.layout.item_list
    }

    override fun bindView(item: Any, position: Int, viewHolder: RecyclerView.ViewHolder) {
        viewHolder as GenericViewHolder
        if (item is Movie) {

            val mMovieTitle: TextView? = viewHolder.getView(R.id.movie_title)
            val mMovieDesc: TextView? = viewHolder.getView(R.id.movie_desc)
            val mYear: TextView? = viewHolder.getView(R.id.movie_year)
            val mPosterImg: ImageView? = viewHolder.getView(R.id.movie_poster)


            mMovieTitle?.text = item.title
            mYear?.text = AppHelpers.formatYearLabel(item)
            mMovieDesc?.text = item.overview

            // load movie thumbnail
            AppHelpers.loadImage(context, item.posterPath!!)
                    .into(mPosterImg)
        }
    }


    override fun isHeader(item: Any): Boolean {
        return item is String
    }

    override fun getHeaderLayout(): Int {
        return R.layout.item_expandable_header
    }

    override fun bindHeaderView(item: Any, position: Int, holder: RecyclerView.ViewHolder, isExpanded: Boolean) {
        holder as GenericViewHolder
        if (item is String) {
            val title: TextView? = holder.getView<TextView>(R.id.heder_text)
            title?.text = item.toString()

            val imageView = holder.getView<ImageView>(R.id.arrowImage)

            imageView?.rotation = if (isExpanded) 0f else -90f
        }
    }

    override fun onCollapse(position: Int) {
        (weakLayoutManager.get()?.findViewByPosition(position))
                ?.findViewById<ImageView>(R.id.arrowImage)
                ?.animate()
                ?.rotation(-90f)
                ?.setDuration(200)
                ?.start()
    }

    override fun onExpand(position: Int) {
        (weakLayoutManager.get()?.findViewByPosition(position))
                ?.findViewById<ImageView>(R.id.arrowImage)
                ?.animate()
                ?.rotation(0f)
                ?.setDuration(200)
                ?.start()
    }

}


