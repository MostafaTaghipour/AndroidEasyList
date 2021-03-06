package ir.rainyday.listexample.modules.sectioned

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.rainyday.easylist.RecyclerViewAdapter
import ir.rainyday.easylist.GenericViewHolder
import com.l4digital.fastscroll.FastScroller
import ir.rainyday.listexample.AppHelpers
import ir.rainyday.listexample.R
import ir.rainyday.listexample.model.Movie
import ir.rainyday.easylist.PinnedHeaderItemDecoration
import ir.rainyday.easylist.SectionedAdapter


/**
 * Created by mostafa-taghipour on 12/22/17.
 */




class SectionedAdapter(context: Context): RecyclerViewAdapter<Any>(),
        SectionedAdapter,
        PinnedHeaderItemDecoration.PinnedHeaderAdapter,
        FastScroller.SectionIndexer{

    override fun getLayout(viewType: Int): Int {
        return  R.layout.item_movie
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
            AppHelpers.loadImage(viewHolder.itemView.context, item.posterPath!!)
                    .into(mPosterImg)
        }
    }


    //header
    override fun isHeader(position: Int): Boolean {
        return items!![position] is Char
    }
    override fun getHeaderLayout(): Int {
        return R.layout.item_section_header
    }

    override fun bindHeaderView(item: Any, position: Int, holder: RecyclerView.ViewHolder) {
       holder as GenericViewHolder
        if (item is Char) {
            val title: TextView? = holder.getView(R.id.heder_text)
            title?.text = item.toString()
        }

    }

    //pinned header
    override fun isPinnedViewType(viewType: Int, position: Int): Boolean {
        return isHeader(position)
    }

    //footer
    override fun isFooter(position: Int): Boolean {
        return items!![position] is Int
    }

    override fun getFooterLayout(): Int? {
        return R.layout.item_section_footer
    }

    override fun bindFooterView(item: Any, position: Int, holder: RecyclerView.ViewHolder) {
        holder as GenericViewHolder
        if (item is Int) {
            val title: TextView? = holder.getView<TextView>(R.id.footer_text)
            title?.text = "There are $item movies that started with the letter ${(items!![position-1] as Movie).title!![0]}"
        }
    }


    //indexing
    override fun getSectionText(position: Int): String {

        val item = items!![position]

        var indexText: String

        if (isHeader(position))
            indexText=item.toString()
        else if (isFooter(position))
            indexText=((items!![position-1] as Movie).title!![0]).toString()
        else
            indexText= (item as Movie).title!![0].toString()

        return indexText
    }
}


