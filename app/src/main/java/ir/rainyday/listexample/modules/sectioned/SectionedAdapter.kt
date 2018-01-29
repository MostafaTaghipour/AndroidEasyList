package ir.rainyday.listexample.modules.sectioned

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import ir.rainday.easylist.RecyclerViewAdapter
import ir.rainday.easylist.GenericViewHolder
import com.l4digital.fastscroll.FastScroller
import ir.rainyday.listexample.AppHelpers
import ir.rainyday.listexample.R
import ir.rainyday.listexample.model.Movie
import ir.rainday.easylist.PinnedHeaderItemDecoration
import ir.rainday.easylist.SectionedAdapter


/**
 * Created by mostafa-taghipour on 12/22/17.
 */




class SectionedAdapter(context: Context): RecyclerViewAdapter<Any>(context),
        SectionedAdapter,
        PinnedHeaderItemDecoration.PinnedHeaderAdapter,
        FastScroller.SectionIndexer{

    override fun getLayout(viewType: Int): Int {
        return  R.layout.item_list
    }

    override fun bindView(item: Any, position: Int, viewHolder: RecyclerView.ViewHolder) {
         viewHolder as GenericViewHolder
        if (item is Movie) {

            val mMovieTitle: TextView? = viewHolder.getView<TextView>(R.id.movie_title)
            val mMovieDesc: TextView? = viewHolder.getView<TextView>(R.id.movie_desc)
            val mYear: TextView? = viewHolder.getView<TextView>(R.id.movie_year)
            val mPosterImg: ImageView? = viewHolder.getView<ImageView>(R.id.movie_poster)


            mMovieTitle?.text = item.title
            mYear?.text = AppHelpers.formatYearLabel(item)
            mMovieDesc?.text = item.overview

            // load movie thumbnail
            AppHelpers.loadImage(context, item.posterPath!!)
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
            val title: TextView? = holder.getView<TextView>(R.id.heder_text)
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


