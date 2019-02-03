package ir.rainyday.listexample.modules.layoumanager

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.rainyday.easylist.GenericViewHolder
import ir.rainyday.easylist.RecyclerViewAdapter
import ir.rainyday.listexample.AppHelpers
import ir.rainyday.listexample.DynamicHeightNetworkImageView
import ir.rainyday.listexample.R
import ir.rainyday.listexample.model.Movie

/**
 * Created by mostafa-taghipour on 12/22/17.
 */


class LayoutManagerAdapter(context: Context) : RecyclerViewAdapter<Movie>(context) {

    var layoutType: LayoutType = LayoutType.Linear
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getLayout(viewType: Int): Int {
        return when (viewType) {
            LayoutType.Linear.value -> R.layout.item_list
            LayoutType.Grid.value -> R.layout.item_grid
            LayoutType.Spanned.value -> R.layout.item_spanned
            LayoutType.Staggered.value -> R.layout.item_staggered
            LayoutType.Flow.value -> R.layout.item_flow
            else -> 0
        }
    }

    override fun bindView(item: Movie, position: Int, viewHolder: RecyclerView.ViewHolder) {
        viewHolder as GenericViewHolder
        val itemType = getItemType(position)

        if (itemType == LayoutType.Linear.value) {

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
        } else if (itemType == LayoutType.Grid.value) {

            val mMovieTitle: TextView? = viewHolder.getView(R.id.movie_title)
            val mPosterImg: ImageView = viewHolder.getView(R.id.movie_poster)!!


            mMovieTitle?.text = item.title

            // load movie thumbnail
            AppHelpers.loadImage(context, item.posterPath!!)
                    .into(mPosterImg)

        } else if (itemType == LayoutType.Spanned.value) {

            val mMovieTitle: TextView? = viewHolder.getView(R.id.movie_title)
            val mPosterImg: ImageView = viewHolder.getView(R.id.movie_poster)!!
            val mContainer: View = viewHolder.getView(R.id.container)!!

            mMovieTitle?.text = item.title

            // load movie thumbnail
            AppHelpers.loadImage(context, item.posterPath!!)
                    .into(mPosterImg)

            mContainer.post {
                val layoutParams = mContainer.layoutParams
                val height = if (position > 3) 150 else 250
                layoutParams.height = Math.round(height * (context.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
                mContainer.layoutParams = layoutParams
                mMovieTitle?.setTextSize(TypedValue.COMPLEX_UNIT_SP,if (position > 3) 9f else 14f)
            }


        } else if (itemType == LayoutType.Staggered.value) {
            val mPosterImg = viewHolder.getView<DynamicHeightNetworkImageView>(R.id.movie_poster)

            // load movie thumbnail
            AppHelpers.loadImage(context, item.posterPath!!)
                    .into(mPosterImg)

            mPosterImg?.setAspectRatio(item.ratio)

        }

        else{
            val mMovieTitle: TextView? = viewHolder.getView(R.id.title)
            val mPosterImg: ImageView = viewHolder.getView(R.id.image)!!


            mMovieTitle?.text = item.title

            // load movie thumbnail
            AppHelpers.loadImage(context, item.posterPath!!)
                    .into(mPosterImg)
        }
    }


    override fun getItemType(position: Int): Int {
        return layoutType.value
    }

}


